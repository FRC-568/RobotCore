package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.team568.robot.RobotBase;

public class TalonSRXDrive extends DriveBase {
	private static final double SPEED_MAX = 1.0;
	private static final double SPEED_SAFE = 0.5;

	private final double TICKS_PER_METER = 100;
	private final double METERS_PER_TICK = 1 / TICKS_PER_METER;

	private boolean _safeMode = Preferences.getBoolean("Safe Mode", false);
	private boolean _isReversed = Preferences.getBoolean("Reverse Direction", false);
	private boolean _tankControls = Preferences.getBoolean("Tank Controls", false);

	public final DifferentialDrive drive;
	private ADXRS450_Gyro gyro;
	private WPI_TalonSRX[] motorsL;
	private WPI_TalonSRX[] motorsR;

	private DifferentialDriveOdometry odometry;

	public TalonSRXDrive(final RobotBase robot) {
		super(robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);
	}

	private DifferentialDrive buildDrive() {
		boolean invert;
		int[] ports;

		ports = configIntArray("leftMotors");
		invert = configBoolean("leftInverted");
		motorsL = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsL.length; i++) {
			motorsL[i] = new WPI_TalonSRX(ports[i]);
			motorsL[i].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
			motorsL[i].setInverted(invert);
			motorsL[i].setNeutralMode(NeutralMode.Coast);
			motorsL[i].configOpenloopRamp(1);
			motorsL[i].configPeakCurrentLimit(20);
			motorsL[i].configContinuousCurrentLimit(27);
			if (i > 0) motorsL[i].follow(motorsL[0]);
		}

		ports = configIntArray("rightMotors");
		invert = configBoolean("rightInverted");
		motorsR = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsR.length; i++) {
			motorsR[i] = new WPI_TalonSRX(ports[i]);
			motorsR[i].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
			motorsR[i].setInverted(invert);
			motorsR[i].setNeutralMode(NeutralMode.Coast);
			motorsL[i].configOpenloopRamp(1);
			motorsR[i].configPeakCurrentLimit(20);
			motorsR[i].configContinuousCurrentLimit(27);
			if (i > 0)
				motorsR[i].follow(motorsR[0]);
		}

		DifferentialDrive d = new DifferentialDrive(motorsL[0], motorsR[0]);
		SendableRegistry.addChild(this, d);

		odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()), 0, 0);
	 
		return d;
	}

	
	@Override
	public void periodic() {
		// Update the odometry in the periodic block
		odometry.update(Rotation2d.fromDegrees(getHeading()), getDistanceInTicks(),
						getDistanceInTicks());
	}

	/**
	 * Returns the currently-estimated pose of the robot.
	 *
	 * @return The pose.
	 */
	public Pose2d getPose() {
		return odometry.getPoseMeters();
	}

	/**
	 * Returns the current wheel speeds of the robot.
	 *
	 * @return The current wheel speeds.
	 */
	public DifferentialDriveWheelSpeeds getWheelSpeeds() {
		return new DifferentialDriveWheelSpeeds(motorsL[0].getSelectedSensorVelocity(), motorsR[0].getSelectedSensorVelocity());
	}

	/**
	 * Resets the odometry to the specified pose.
	 *
	 * @param pose The pose to which to set the odometry.
	 */
	public void resetOdometry(Pose2d pose) {
		resetSensors();
		odometry.resetPosition(Rotation2d.fromDegrees(getHeading()), 0, 0, pose);
	}

	/**
	 * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
	 *
	 * @param maxOutput the maximum output to which the drive will be constrained
	 */
	public void setMaxOutput(double maxOutput) {
		drive.setMaxOutput(maxOutput);
	}

	/**
	 * Zeroes the heading of the robot.
	 */
	private void configureEncoder(WPI_TalonSRX talon) {
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		talon.setSensorPhase(true);
	}

	public TalonSRXDrive withGyro(final ADXRS450_Gyro gyro) {
		this.gyro = gyro;
		return this;
	}
	
	@Override
	public void tankDrive(double leftValue, double rightValue, boolean squareInputs) {
		if (getIsReversed()){
			double leftTemp = leftValue;
			leftValue = rightValue * -1;
			rightValue = leftTemp * -1;
		}
		drive.tankDrive(leftValue, rightValue, squareInputs);
	}

	public void tankDriveVolts(double leftVolts, double rightVolts) {
		motorsL[0].setVoltage(leftVolts);
		motorsR[0].setVoltage(-rightVolts);
		drive.feed();
	  }

	@Override
	public void arcadeDrive(double moveValue, double rotateValue, boolean squareInputs) {
		if (getIsReversed())
			moveValue *= -1;
		drive.arcadeDrive(moveValue, rotateValue, squareInputs);
	}

	@Override
	public double getHeading() {
		return gyro != null ? gyro.getAngle() : 0;
	}

	public double getTurnRate() {
		return gyro != null ? gyro.getRate() : 0;
	}

	public void zeroHeading() {
		if (gyro != null)
			gyro.reset();
	}

	/**
	 * Returns the heading of the robot.
	 *
	 * @return the robot's heading in degrees, from -180 to 180
	 */
	//public double getHeading() {
		//return Math.IEEEremainder(gyro.getAngle(), 360) * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
	//}

	/**
	 * Returns the turn rate of the robot.
	 *
	 * @return The turn rate of the robot, in degrees per second
	 */
	//public double getTurnRate() {
		//return gyro.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
	//}
	

	@Override
	public double getVelocityInTicks() {
		return (motorsL[0].getSelectedSensorVelocity() + motorsR[0].getSelectedSensorVelocity()) / 2;
	}

	@Override
	public double getVelocityInTicks(Side side) {
		return side == Side.RIGHT ? motorsR[0].getSelectedSensorVelocity() : motorsL[0].getSelectedSensorVelocity();
	}

	@Override
	public double getDistanceInTicks() {
		return (motorsL[0].getSelectedSensorPosition() + motorsR[0].getSelectedSensorPosition()) / 2;
	}

	@Override
	public double getDistanceInTicks(Side side) {
		return side == Side.RIGHT ? motorsR[0].getSelectedSensorPosition() : motorsL[0].getSelectedSensorPosition();
	}

	public double getDistanceInMeters() {
		return getDistanceInTicks() * METERS_PER_TICK;
	}

	public double getDistanceInMeters(Side side) {
		return getDistanceInTicks(side) * METERS_PER_TICK;
	}

	@Override
	public void resetSensors() {
		motorsL[0].setSelectedSensorPosition(0);
		motorsR[0].setSelectedSensorPosition(0);
	}

	public void setSpeed(double speed) {
		drive.arcadeDrive(speed, 0);
	}

	public void halt() {
		drive.stopMotor();
	}

	public boolean getIsReversed() {
		return _isReversed;
	}

	public void setIsReversed(boolean reverse) {
		_isReversed = reverse;
		Preferences.setBoolean("Reverse Direction", reverse);
	}

	public void toggleIsReversed() {
		setIsReversed(!getIsReversed());;
	}

	public boolean getTankControls() {
		return _tankControls;
	}

	public void setTankControls(boolean reverse) {
		_tankControls = reverse;
		Preferences.setBoolean("Tank Controls", reverse);
	}

	public void toggleTankControls() {
		setTankControls(!getTankControls());;
	}


	public boolean getSafeMode() {
		return _safeMode;
	}

	public void setSafeMode(boolean enabled) {
		_safeMode = enabled;
		drive.setMaxOutput(enabled ? SPEED_SAFE : SPEED_MAX);
		System.out.println("Safe Mode is " + (enabled ? "Enabled" : "Disabled") + ".");
		Preferences.setBoolean("Safe Mode", enabled);
	}

	public void toggleSafeMode() {
		setSafeMode(!getSafeMode());
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addDoubleProperty("Left Velocity", () -> getVelocityInTicks(Side.LEFT), null);
		builder.addDoubleProperty("Right Velocity", () -> getVelocityInTicks(Side.RIGHT), null);
		builder.addDoubleProperty("Average Velocity", () -> getVelocityInTicks(), null);
		builder.addDoubleProperty("Average Distance", () -> getDistanceInTicks(), null);
		builder.addBooleanProperty("Safe Mode", this::getSafeMode, this::setSafeMode);
		builder.addBooleanProperty("Reverse Direction", this::getIsReversed, this::setIsReversed);
		builder.addBooleanProperty("Tank Controls", this::getTankControls, this::setTankControls);
	}

}