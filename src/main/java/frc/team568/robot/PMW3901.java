/*
 * Driver for the Pixart PMW3901 optical flow sensor, as used in the Bitcraze Flow Breakout board.
 */

package frc.team568.robot;

import static frc.team568.robot.PMW3901.Magic.INVERSE_PRODUCT_ID_CODE;
import static frc.team568.robot.PMW3901.Magic.POWER_UP_RESET_CODE;
import static frc.team568.robot.PMW3901.Magic.PRODUCT_ID_CODE;
import static frc.team568.robot.PMW3901.Register.DELTA_X_H;
import static frc.team568.robot.PMW3901.Register.DELTA_X_L;
import static frc.team568.robot.PMW3901.Register.DELTA_Y_H;
import static frc.team568.robot.PMW3901.Register.DELTA_Y_L;
import static frc.team568.robot.PMW3901.Register.INVERSE_PRODUCT_ID;
import static frc.team568.robot.PMW3901.Register.MOTION;
import static frc.team568.robot.PMW3901.Register.POWER_UP_RESET;
import static frc.team568.robot.PMW3901.Register.PRODUCT_ID;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.util.Vector2;

// Temporarily hide 2020 deprecation warnings
@SuppressWarnings("all")
public class PMW3901 extends SendableBase {
	private static final CLibrary c = CLibrary.INSTANCE;
	private static final HalLibrary hal = HalLibrary.INSTANCE;
	private static final int SPI_CLOCK = 2000000;
	private static final double ms = 1.0E-3; // milliseconds
	private static final double us = 1.0E-6; // microseconds
	private SPI spi;
	private SPI.Port spiPort;
	private Spi_Ioc_Transfer[] iocStruct;
	private Memory txBuffer;
	private Thread autoLoop;
	private boolean autoEnabled;

	protected Vector2 position;
	protected Vector2 velocity;
	protected Vector2 acceleration;
	protected double timestamp;
	protected double processTime;

	public static boolean verbose = false;
	public double updateInterval = 20 * ms;

	public PMW3901(SPI.Port port) {
		spiPort = port;
		spi = new SPI(port);
		initialize();
		setName("PMW3901", port.value);
	}

	public void initialize() {
		/*
		 * PMW3901 officially supports a 2MHz clock; but may need more time between the
		 * address byte and data byte on read commands (35uS). If read commands are
		 * failing, lower this to ~14kHz.
		 */
		spi.setClockRate(SPI_CLOCK);
		spi.setMSBFirst();
		spi.setSampleDataOnTrailingEdge();
		spi.setClockActiveLow();
		spi.setChipSelectActiveLow();

		txBuffer = new Memory(2);
		txBuffer.clear();

		iocStruct = (Spi_Ioc_Transfer[]) new Spi_Ioc_Transfer().toArray(2);

		iocStruct[0].clear();
		iocStruct[0].speed_hz = SPI_CLOCK;
		iocStruct[0].bits_per_word = 8;
		iocStruct[0].delay_usecs = 50;
		iocStruct[0].tx_buf = Pointer.nativeValue(txBuffer);
		iocStruct[0].rx_buf = Pointer.nativeValue(txBuffer);
		iocStruct[0].len = 1;
		iocStruct[0].write();

		iocStruct[1].clear();
		iocStruct[1].speed_hz = SPI_CLOCK;
		iocStruct[1].bits_per_word = 8;
		iocStruct[1].delay_usecs = 50;
		iocStruct[1].tx_buf = Pointer.nativeValue(txBuffer);
		iocStruct[1].rx_buf = Pointer.nativeValue(txBuffer);
		iocStruct[1].len = 1;
		iocStruct[1].write();

		// Reset state - power-up delay is 50ms
		writeByte(POWER_UP_RESET, POWER_UP_RESET_CODE);
		Timer.delay(50 * ms);

		// Verify device is PMW3901
		if (readByte(PRODUCT_ID) != PRODUCT_ID_CODE || readByte(INVERSE_PRODUCT_ID) != INVERSE_PRODUCT_ID_CODE) {
			spi.close();
			spi = null;
			DriverStation.reportError("Could not create PMW3901: sensor not detected on SPI port " + spiPort.value,
					false);
		}

		initializeRegisters();
		reset();
	}

	public void reset() {
		// Clear motion registers
		readByte(MOTION);
		readByte(DELTA_X_L);
		readByte(DELTA_X_H);
		readByte(DELTA_Y_L);
		readByte(DELTA_Y_H);

		timestamp = Timer.getFPGATimestamp();
		processTime = 0;
		position = Vector2.zero;
		velocity = Vector2.zero;
		acceleration = Vector2.zero;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public Vector2 readMotion() {
		readByte(MOTION);
		short xValue = readByte(DELTA_X_H);
		xValue <<= 8;
		xValue |= readByte(DELTA_X_L);

		short yValue = readByte(DELTA_Y_H);
		yValue <<= 8;
		yValue |= readByte(DELTA_Y_L);

		return Vector2.of(xValue, yValue);
	}

	public Vector2 updateMotion() {
		double currentTime = Timer.getFPGATimestamp();
		double deltaTime = currentTime - timestamp;
		Vector2 motion = readMotion();

		position = position.add(motion);

		Vector2 currentVelocity = motion.scale(1 / deltaTime);
		acceleration = currentVelocity.subtract(velocity).scale(1 / deltaTime);
		velocity = currentVelocity;
		timestamp = currentTime;
		processTime = Timer.getFPGATimestamp() - currentTime;

		return motion;
	}

	public void writeByte(byte register, byte value) {
		if (spi == null)
			return;

		txBuffer.setByte(0, (byte) (register | 0x80));
		txBuffer.setByte(1, value);
		iocStruct[0].writeField("len", 2);
		c.ioctl(hal.HAL_GetSPIHandle(spiPort.value), CLibrary.Spi_Ioc_Message(1), iocStruct[0].getPointer());

		log("Wrote 0x%X to 0x%X", value, register);
		Timer.delay(200 * us);
	}

	public byte readByte(byte register) {
		if (spi == null)
			return 0;

		txBuffer.setByte(0, (byte) (register & 0x7F));
		iocStruct[0].writeField("len", 1);
		c.ioctl(hal.HAL_GetSPIHandle(spiPort.value), CLibrary.Spi_Ioc_Message(2), iocStruct[0].getPointer());

		byte value = txBuffer.getByte(0);

		log("Read 0x%X from 0x%X", value, register);
		Timer.delay(200 * us);
		return value;
	}

	public void startAutoLoop() {
		stopAutoLoop();
		autoEnabled = true;
		autoLoop = new Thread(() -> {
			while (autoEnabled) {
				updateMotion();
				try {
					if (processTime < updateInterval)
						Thread.sleep((long) ((updateInterval - processTime) * 1000));
				} catch (InterruptedException e) {
				}
			}
		}, "PMW3901-AutoLooop-" + Thread.activeCount());
		autoLoop.start();
	}

	public void stopAutoLoop() {
		autoEnabled = false;
		if (autoLoop != null) {
			if (autoLoop.isAlive() && !autoLoop.isInterrupted())
				autoLoop.interrupt();
			try {
				autoLoop.join((long) (updateInterval / ms));
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void close() {
		super.close();
		if (spi != null) {
			spi.close();
			spi = null;
		}
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType(getName());
		NetworkTableEntry pX = builder.getEntry("X");
		NetworkTableEntry pY = builder.getEntry("Y");
		NetworkTableEntry vX = builder.getEntry("vX");
		NetworkTableEntry vY = builder.getEntry("vY");
		NetworkTableEntry aX = builder.getEntry("aX");
		NetworkTableEntry aY = builder.getEntry("aY");

		builder.setUpdateTable(() -> {
			pX.setDouble(position.x);
			pY.setDouble(position.y);
			vX.setDouble(velocity.x);
			vY.setDouble(velocity.y);
			aX.setDouble(acceleration.x);
			aY.setDouble(acceleration.y);
		});
	}

	private static void log(String message, Object... args) {
		if (verbose)
			System.out.println(String.format("PMW3901: " + message, args));
	}

	private void initializeRegisters() {
		final byte[] optReg1 = { 0x7F, 0x61, 0x7F, 0x40, 0x7F, 0x41, 0x43, 0x45, 0x5B, 0x5F, 0x7B, 0x7F, 0x44, 0x40,
				0x4E, 0x7F, 0x65, 0x6A, 0x7F, 0x4F, 0x5F, 0x48, 0x49, 0x57, 0x60, 0x61, 0x62, 0x63, 0x7F, 0x45, 0x7F,
				0x4D, 0x55, 0x74, 0x75, 0x4A, 0x4B, 0x44, 0x45, 0x64, 0x65, 0x7F, 0x65, 0x66, 0x63, 0x7F, 0x48, 0x7F,
				0x41, 0x43, 0x4B, 0x45, 0x44, 0x4C, 0x7F, 0x5B, 0x7F, 0x40, 0x70 };
		final byte[] optVal1 = { 0x00, (byte) 0xAD, 0x03, 0x00, 0x05, (byte) 0xB3, (byte) 0xF1, 0x14, 0x32, 0x34, 0x08,
				0x06, 0x1B, (byte) 0xBF, 0x3F, 0x08, 0x20, 0x18, 0x09, (byte) 0xAF, 0x40, (byte) 0x80, (byte) 0x80,
				0x77, 0x78, 0x78, 0x08, 0x50, 0x0A, 0x60, 0x00, 0x11, (byte) 0x80, 0x1F, 0x1F, 0x78, 0x78, 0x08, 0x50,
				(byte) 0xFF, 0x1F, 0x14, 0x60, 0x08, 0x78, 0x15, 0x58, 0x07, 0x0D, 0x14, 0x0E, 0x0F, 0x42, (byte) 0x80,
				0x10, 0x02, 0x07, 0x41, 0x00 };
		for (int i = 0; i < optReg1.length; i++)
			writeByte(optReg1[i], optVal1[i]);

		Timer.delay(1);

		final byte[] optReg2 = { 0x32, 0x7F, 0x40, 0x7F, 0x62, 0x63, 0x7F, 0x48, 0x6F, 0x7F, 0x5B, 0x4E, 0x5A, 0x40 };
		final byte[] optVal2 = { 0x44, 0x07, 0x40, 0x06, (byte) 0xf0, 0x00, 0x0D, (byte) 0xC0, (byte) 0xd5, 0x00,
				(byte) 0xa0, (byte) 0xA8, 0x50, (byte) 0x80 };
		for (int i = 0; i < optReg2.length; i++)
			writeByte(optReg1[i], optVal2[i]);
	}

	public static final class Register {
		public static final byte PRODUCT_ID = 0x00; // RO - initial 0x49
		public static final byte REVISION_ID = 0x01; // RO - initial 0x00
		public static final byte MOTION = 0x02; // RW - initial 0x00
		public static final byte DELTA_X_L = 0x03; // RO - initial 0x00
		public static final byte DELTA_X_H = 0x04; // RO - initial 0x00
		public static final byte DELTA_Y_L = 0x05; // RO - initial 0x00
		public static final byte DELTA_Y_H = 0x06; // RO - initial 0x00
		public static final byte SQUAL = 0x07; // RO - initial 0x00
		public static final byte RAWDATA_SUM = 0x08; // RO - initial 0x00
		public static final byte MAXIMUM_RAWDATA = 0x09; // RO - initial 0x00
		public static final byte MINIMUM_RAWDATA = 0x0A; // RO - initial 0x00
		public static final byte SHUTTER_LOWER = 0x0B; // RO - initial 0x00
		public static final byte SHUTTER_UPPER = 0x0C; // RO - initial 0x00

		public static final byte OBSERVATION = 0x15; // RW - initial 0x00
		public static final byte MOTION_BURST = 0x16; // RO - initial 0x00

		public static final byte POWER_UP_RESET = 0x3A; // WO - initial N/A
		public static final byte SHUTDOWN = 0x3B; // WO - initial N/A

		public static final byte RAWDATA_GRAB = 0x58; // RW - initial 0x00
		public static final byte RAWDATA_GRAB_STATUS = 0x59; // RO - initial 0x00
		public static final byte INVERSE_PRODUCT_ID = 0x5F; // RO - initial 0xB6
	}

	public static final class Magic {
		public static final byte POWER_UP_RESET_CODE = 0x5A; // write to 0x3A to restart
		public static final byte PRODUCT_ID_CODE = 0x49; // should match register 0x00
		public static final byte INVERSE_PRODUCT_ID_CODE = (byte) 0xB6; // ~0x49
	}

	protected interface CLibrary extends Library {
		CLibrary INSTANCE = Native.loadLibrary("c", CLibrary.class);

		public static int Spi_Ioc_Message(int size) {
			final int _IOC_SIZEBITS = 14;
			final int SPI_IOC_MAGIC = 0x6B; // ASCII 'k'
			final int MSG_SIZE = 32; // sizeof(struct spi_ioc_transfer) in bytes

			int spi_msgsize = (((size * MSG_SIZE) < (1 << _IOC_SIZEBITS)) ? (size * MSG_SIZE) : 0);
			return (1 << 30) | (SPI_IOC_MAGIC << 8) | (spi_msgsize << 16);
		}

		public int ioctl(int fd, int cmd, Pointer arg);

		public int open(String path, int flags);

		public int close(int fd);
	}

	protected interface HalLibrary extends Library {
		HalLibrary INSTANCE = Native.loadLibrary("wpiHal", HalLibrary.class);

		public int HAL_GetSPIHandle(int port);
	}

	public static class Spi_Ioc_Transfer extends Structure {
		public long tx_buf;
		public long rx_buf;
		public int len;
		public int speed_hz;
		public short delay_usecs;
		public byte bits_per_word;
		public byte cs_change;
		public int pad;

		public Spi_Ioc_Transfer() {
			super(ALIGN_NONE);
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("tx_buf", "rx_buf", "len", "speed_hz", "delay_usecs", "bits_per_word", "cs_change",
					"pad");
		}

		public static class ByReference extends Spi_Ioc_Transfer implements Structure.ByReference {
		}

		public static class ByValue extends Spi_Ioc_Transfer implements Structure.ByValue {
		}
	}

}
