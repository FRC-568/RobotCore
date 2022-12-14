package frc.team568.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MechyDrive extends SubsystemBase implements OmniDriveSubsystem {
	public static double kP_Drift = 0.0125;
	public static double inputDeadband = 0.05;

	protected MecanumDrive drive;
	protected Gyro gyro;

	protected boolean m_fieldPOVEnabled = false;
	protected boolean m_safeModeEnabled = false;
    protected boolean m_driftCorrectionEnabled = false;
	protected double m_baseHeading = 0;

	public MechyDrive(int motorFL, int motorBL, int motorFR, int motorBR) {
		initMotors(motorFL, motorBL, motorFR, motorBR);
	}

	private void initMotors(int motorFL, int motorBL, int motorFR, int motorBR) {
		WPI_TalonSRX fl = new WPI_TalonSRX(motorFL);
		WPI_TalonSRX bl = new WPI_TalonSRX(motorBL);
		WPI_TalonSRX fr = new WPI_TalonSRX(motorFR);
		WPI_TalonSRX br = new WPI_TalonSRX(motorBR);

		fl.setInverted(false);
		bl.setInverted(false);
		fr.setInverted(true);
		br.setInverted(true);

		drive = new MecanumDrive(fl, bl, fr, br);
		drive.setDeadband(inputDeadband);
		addChild("drive", drive);
	}

	public MechyDrive useADXRS450_Gyro() {
		return useGyro(new ADXRS450_Gyro());
	}

	public MechyDrive useGyro(Gyro gyro) {
		this.gyro = gyro;
		resetHeading();
		return this;
	}

	public Gyro getGyro() {
		return gyro;
	}

	@Override
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
		if (usingDriftCorrection())
			zRotation -= gyro.getRate() * kP_Drift;
		drive.driveCartesian(ySpeed, xSpeed, zRotation);
	}

	@Override
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
		if (usingDriftCorrection())
			zRotation -= gyro.getRate() * kP_Drift;
		drive.driveCartesian(ySpeed, xSpeed, zRotation, gyroAngle);
	}

	@Override
	public void drivePolar(double magnitude, double angle, double zRotation) {
		if (usingDriftCorrection())
			zRotation -= gyro.getRate() * kP_Drift;
		drive.drivePolar(magnitude, angle, zRotation);
	}

	@Override
	public void stopMotor() {
		drive.stopMotor();
	}

	public double getHeading() {
		if (gyro == null)
			return 0;
		return gyro.getAngle() - m_baseHeading;
	}

	public void setHeading(double newHeading) {
		if (gyro == null)
			m_baseHeading = newHeading;
		else
			m_baseHeading = gyro.getAngle() - newHeading;
	}

	public void resetHeading() {
		if (gyro == null)
			m_baseHeading = 0;
		else
			m_baseHeading = gyro.getAngle();
	}

	public boolean usingFieldPOV() {
		return m_fieldPOVEnabled;
	}

	public void setFieldPOV(boolean enabled) {
		m_fieldPOVEnabled = enabled;
		resetHeading();
	}

	public boolean toggleFieldPOV() {
		setFieldPOV(!usingFieldPOV());
		return usingFieldPOV();
	}

	public boolean usingSafeMode() {
		return m_safeModeEnabled;
	}

	public void setSafeMode(boolean enabled) {
		m_safeModeEnabled = enabled;
		drive.setMaxOutput(enabled ? 0.5 : 1.0);
		System.out.println("Safemode is " + (m_safeModeEnabled ? "Enabled" : "Disabled") + ".");
	}

	public boolean toggleSafeMode() {
		setSafeMode(!usingSafeMode());
		return usingSafeMode();
	}

	public boolean usingDriftCorrection() {
		return m_driftCorrectionEnabled;
	}

	public void setDriftCorrection(boolean enabled) {
		m_driftCorrectionEnabled = enabled;
	}

	public boolean toggleDriftCorrection() {
		setDriftCorrection(!usingDriftCorrection());
		return usingDriftCorrection();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addDoubleProperty("Heading", this::getHeading, this::setHeading);
		builder.addBooleanProperty("FieldPOV", this::usingFieldPOV, this::setFieldPOV);
		builder.addBooleanProperty("SafeMode", this::usingSafeMode, this::setSafeMode);
		builder.addDoubleProperty("kP_Drift", () -> kP_Drift, v -> kP_Drift = v);
	}

	public ControlBuilder buildControlCommand() {
		return new ControlBuilder();
	}

	public final class ControlBuilder {
		private DoubleSupplier forwardAxis;
		private DoubleSupplier sideAxis;
		private DoubleSupplier turnAxis;

		private ControlBuilder() {
			DoubleSupplier defaultZero = () -> 0;
			forwardAxis = defaultZero;
			sideAxis = defaultZero;
			turnAxis = defaultZero;
		}

		public ControlBuilder withForwardAxis(DoubleSupplier forward) {
			this.forwardAxis = forward;
			return this;
		}

		public ControlBuilder withSideAxis(DoubleSupplier side) {
			this.sideAxis = side;
			return this;
		}

		public ControlBuilder withTurnAxis(DoubleSupplier turn) {
			this.turnAxis = turn;
			return this;
		}

		public Command makeCommand() {
			return new RunCommand(() -> {
				driveCartesian(
					forwardAxis.getAsDouble(),
					sideAxis.getAsDouble(),
					turnAxis.getAsDouble(),
					usingFieldPOV() ? getHeading() : 0);
			}, MechyDrive.this);
		}

		public MechyDrive makeDefault() {
			setDefaultCommand(makeCommand());
			return MechyDrive.this;
		}
	}

}
