/*
 * Driver for the Pixart PMW3901 optical flow sensor, as used in the Bitcraze Flow Breakout board.
 */

package org.usfirst.frc.team568.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import static org.usfirst.frc.team568.robot.PMW3901.Register.*;
import static org.usfirst.frc.team568.robot.PMW3901.Magic.*;

public class PMW3901 extends SensorBase implements Sendable {
	protected SPI spi;
	protected SPI.Port spiPort;
	protected Vector2d position;
	protected Vector2d velocity;
	protected Vector2d acceleration;
	protected double timestamp;
	protected double processTime;
	private Thread autoLoop;
	private boolean autoEnabled;
	private byte[] readBuffer = new byte[2];
	private byte[] writeBuffer = new byte[2];
	private static final double ms = 1.0E-3; //milliseconds
	private static final double us = 1.0E-6; //microseconds
	public static boolean verbose = false;
	public double updateInterval = 20 * ms;
	
	public PMW3901(SPI.Port port) {
		spiPort = port;
		spi = new SPI(port);
		initialize();
		setName("PMW3901", port.value);
	}
	
	public void initialize() {
		spi.setClockRate(2000000);
		spi.setMSBFirst();
		spi.setSampleDataOnRising();
		spi.setClockActiveLow();
		spi.setChipSelectActiveLow();
		
		// Reset state - power-up delay is 50ms
		writeByte(POWER_UP_RESET, POWER_UP_RESET_CODE);
		Timer.delay(50 * ms);
		
		// Verify device is PMW3901
		if(readByte(PRODUCT_ID) != PRODUCT_ID_CODE || readByte(INVERSE_PRODUCT_ID) != INVERSE_PRODUCT_ID_CODE)
			throw new RuntimeException("Could not create PMW3901: sensor not detected on SPI port " + spiPort.value);
		
		// Clear motion registers
		readByte(MOTION);
		readByte(DELTA_X_L);
		readByte(DELTA_X_H);
		readByte(DELTA_Y_L);
		readByte(DELTA_Y_H);
		
		initializeRegisters();
		
		timestamp = Timer.getFPGATimestamp();
		processTime = 0;
		position = new Vector2d();
		velocity = new Vector2d();
		acceleration = new Vector2d();
	}
	
	public Vector2d updateMotion() {
		double currentTime = Timer.getFPGATimestamp();
		double deltaTime = currentTime - timestamp;
		readByte(MOTION);
		short xValue = readByte(DELTA_X_H);
		xValue <<= 8;
		xValue |= readByte(DELTA_X_L);
		
		short yValue = readByte(DELTA_Y_H);
		yValue <<= 8;
		yValue |= readByte(DELTA_Y_L);

		Vector2d motion = new Vector2d(xValue, yValue);
		position.x = position.x + motion.x;
		position.y = position.y + motion.y;
		
		Vector2d currentVelocity = new Vector2d(motion.x / deltaTime, motion.y / deltaTime);
		acceleration.x = (currentVelocity.x - velocity.x) / deltaTime;
		acceleration.y = (currentVelocity.y - velocity.y) / deltaTime;
		velocity = currentVelocity;
		timestamp = currentTime;
		processTime = Timer.getFPGATimestamp() - currentTime;
		
		return motion;
	}
	
	public void writeByte(byte register, byte value) {
		writeBuffer[0] = (byte) (register | (byte) 0x80);
		writeBuffer[1] = value;
		spi.write(writeBuffer, 2);
		log("Wrote 0x%X to 0x%X", value, register);
		Timer.delay(200 * us);
	}
	
	public byte readByte(byte register) {
		writeBuffer[0] = (byte) (register & 0x7F);
		spi.write(writeBuffer, 1);
		Timer.delay(50 * us);
		spi.read(true, readBuffer, 1);
		log("Read 0x%X from 0x%X", readBuffer[0], register);
		Timer.delay(200 * us);
		return readBuffer[0];
	}
	
	public void startAutoLoop() {
		stopAutoLoop();
		autoEnabled = true;
		autoLoop = new Thread(() -> {
			try {
				updateMotion();
				if(processTime < updateInterval)
					Thread.sleep((long) ((updateInterval - processTime) * 1000));
			} catch(InterruptedException e) {}
		}, "PMW3901-AutoLooop-" + Thread.activeCount());
	}
	
	public void stopAutoLoop() {
		autoEnabled = false;
		if(autoLoop != null) {
			if(autoLoop.isAlive() && !autoLoop.isInterrupted())
				autoLoop.interrupt();
			try {
				autoLoop.join((long) (updateInterval / ms));
			} catch(InterruptedException e) {}
		}
	}
	
	@Override
	public void free() {
		super.free();
		spi.free();
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
		if(verbose)
			System.out.println(String.format("PMW3901: " + message, args));
			
	}
	
	private void initializeRegisters() {
		  writeByte((byte) 0x7F, (byte) 0x00);
		  writeByte((byte) 0x61, (byte) 0xAD);
		  writeByte((byte) 0x7F, (byte) 0x03);
		  writeByte((byte) 0x40, (byte) 0x00);
		  writeByte((byte) 0x7F, (byte) 0x05);
		  writeByte((byte) 0x41, (byte) 0xB3);
		  writeByte((byte) 0x43, (byte) 0xF1);
		  writeByte((byte) 0x45, (byte) 0x14);
		  writeByte((byte) 0x5B, (byte) 0x32);
		  writeByte((byte) 0x5F, (byte) 0x34);
		  writeByte((byte) 0x7B, (byte) 0x08);
		  writeByte((byte) 0x7F, (byte) 0x06);
		  writeByte((byte) 0x44, (byte) 0x1B);
		  writeByte((byte) 0x40, (byte) 0xBF);
		  writeByte((byte) 0x4E, (byte) 0x3F);
		  writeByte((byte) 0x7F, (byte) 0x08);
		  writeByte((byte) 0x65, (byte) 0x20);
		  writeByte((byte) 0x6A, (byte) 0x18);
		  writeByte((byte) 0x7F, (byte) 0x09);
		  writeByte((byte) 0x4F, (byte) 0xAF);
		  writeByte((byte) 0x5F, (byte) 0x40);
		  writeByte((byte) 0x48, (byte) 0x80);
		  writeByte((byte) 0x49, (byte) 0x80);
		  writeByte((byte) 0x57, (byte) 0x77);
		  writeByte((byte) 0x60, (byte) 0x78);
		  writeByte((byte) 0x61, (byte) 0x78);
		  writeByte((byte) 0x62, (byte) 0x08);
		  writeByte((byte) 0x63, (byte) 0x50);
		  writeByte((byte) 0x7F, (byte) 0x0A);
		  writeByte((byte) 0x45, (byte) 0x60);
		  writeByte((byte) 0x7F, (byte) 0x00);
		  writeByte((byte) 0x4D, (byte) 0x11);
		  writeByte((byte) 0x55, (byte) 0x80);
		  writeByte((byte) 0x74, (byte) 0x1F);
		  writeByte((byte) 0x75, (byte) 0x1F);
		  writeByte((byte) 0x4A, (byte) 0x78);
		  writeByte((byte) 0x4B, (byte) 0x78);
		  writeByte((byte) 0x44, (byte) 0x08);
		  writeByte((byte) 0x45, (byte) 0x50);
		  writeByte((byte) 0x64, (byte) 0xFF);
		  writeByte((byte) 0x65, (byte) 0x1F);
		  writeByte((byte) 0x7F, (byte) 0x14);
		  writeByte((byte) 0x65, (byte) 0x60);
		  writeByte((byte) 0x66, (byte) 0x08);
		  writeByte((byte) 0x63, (byte) 0x78);
		  writeByte((byte) 0x7F, (byte) 0x15);
		  writeByte((byte) 0x48, (byte) 0x58);
		  writeByte((byte) 0x7F, (byte) 0x07);
		  writeByte((byte) 0x41, (byte) 0x0D);
		  writeByte((byte) 0x43, (byte) 0x14);
		  writeByte((byte) 0x4B, (byte) 0x0E);
		  writeByte((byte) 0x45, (byte) 0x0F);
		  writeByte((byte) 0x44, (byte) 0x42);
		  writeByte((byte) 0x4C, (byte) 0x80);
		  writeByte((byte) 0x7F, (byte) 0x10);
		  writeByte((byte) 0x5B, (byte) 0x02);
		  writeByte((byte) 0x7F, (byte) 0x07);
		  writeByte((byte) 0x40, (byte) 0x41);
		  writeByte((byte) 0x70, (byte) 0x00);

		 Timer.delay(1);
		  writeByte((byte) 0x32, (byte) 0x44);
		  writeByte((byte) 0x7F, (byte) 0x07);
		  writeByte((byte) 0x40, (byte) 0x40);
		  writeByte((byte) 0x7F, (byte) 0x06);
		  writeByte((byte) 0x62, (byte) 0xf0);
		  writeByte((byte) 0x63, (byte) 0x00);
		  writeByte((byte) 0x7F, (byte) 0x0D);
		  writeByte((byte) 0x48, (byte) 0xC0);
		  writeByte((byte) 0x6F, (byte) 0xd5);
		  writeByte((byte) 0x7F, (byte) 0x00);
		  writeByte((byte) 0x5B, (byte) 0xa0);
		  writeByte((byte) 0x4E, (byte) 0xA8);
		  writeByte((byte) 0x5A, (byte) 0x50);
		  writeByte((byte) 0x40, (byte) 0x80);
	}
	
	public static final class Register {
		public static final byte PRODUCT_ID          = 0x00; // RO - initial 0x49
		public static final byte REVISION_ID         = 0x01; // RO - initial 0x00
		public static final byte MOTION              = 0x02; // RW - initial 0x00
		public static final byte DELTA_X_L           = 0x03; // RO - initial 0x00
		public static final byte DELTA_X_H           = 0x04; // RO - initial 0x00
		public static final byte DELTA_Y_L           = 0x05; // RO - initial 0x00
		public static final byte DELTA_Y_H           = 0x06; // RO - initial 0x00
		public static final byte SQUAL               = 0x07; // RO - initial 0x00
		public static final byte RAWDATA_SUM         = 0x08; // RO - initial 0x00
		public static final byte MAXIMUM_RAWDATA     = 0x09; // RO - initial 0x00
		public static final byte MINIMUM_RAWDATA     = 0x0A; // RO - initial 0x00
		public static final byte SHUTTER_LOWER       = 0x0B; // RO - initial 0x00
		public static final byte SHUTTER_UPPER       = 0x0C; // RO - initial 0x00
		
		public static final byte OBSERVATION         = 0x15; // RW - initial 0x00
		public static final byte MOTION_BURST        = 0x16; // RO - initial 0x00
		
		public static final byte POWER_UP_RESET      = 0x3A; // WO - initial N/A
		public static final byte SHUTDOWN            = 0x3B; // WO - initial N/A
		
		public static final byte RAWDATA_GRAB        = 0x58; // RW - initial 0x00
		public static final byte RAWDATA_GRAB_STATUS = 0x59; // RO - initial 0x00
		public static final byte INVERSE_PRODUCT_ID  = 0x5F; // RO - initial 0xB6
	}
	
	protected static final class Magic {
		public static final byte POWER_UP_RESET_CODE     = 0x5A; // write to 0x3A to restart
		public static final byte PRODUCT_ID_CODE         = 0x49; // should match register 0x00
		public static final byte INVERSE_PRODUCT_ID_CODE = (byte) 0xB6; // ~0x49 - should match register 0x5F; but sample code uses 0xB8
	}

}
