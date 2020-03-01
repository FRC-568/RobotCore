package frc.team568.robot.subsystems;

import static edu.wpi.first.networktables.EntryListenerFlags.kImmediate;
import static edu.wpi.first.networktables.EntryListenerFlags.kLocal;
import static edu.wpi.first.networktables.EntryListenerFlags.kNew;
import static edu.wpi.first.networktables.EntryListenerFlags.kUpdate;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import frc.team568.robot.Constants.DriveConstants;
import frc.team568.robot.RobotBase;

public class TalonSRXDrive extends DriveBase {
	private static final double SPEED_MAX = 1.0;
	private static final double SPEED_SAFE = 0.5;
	public final DifferentialDrive drive;
	private Gyro gyro;
	private WPI_TalonSRX[] motorsL;
	private WPI_TalonSRX[] motorsR;
	private NetworkTableEntry reversedEntry;
	private NetworkTableEntry tankControlsEntry;
	private NetworkTableEntry safeModeEntry;

	private SpeedControllerGroup leftMotors;
	private SpeedControllerGroup rightMotors;

	private Encoder leftEncoder;
	private Encoder rightEncoder;

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
			motorsL[i].setInverted(invert);
			motorsL[i].setNeutralMode(NeutralMode.Coast);
			//motorsL[i].configOpenloopRamp(0.2);
			motorsL[i].configPeakCurrentLimit(20);
			motorsL[i].configContinuousCurrentLimit(27);
			if (i > 0)
				motorsL[i].follow(motorsL[0]);
		}

		ports = configIntArray("rightMotors");
		invert = configBoolean("rightInverted");
		motorsR = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsR.length; i++) {
			motorsR[i] = new WPI_TalonSRX(ports[i]);
			motorsR[i].setInverted(invert);
			motorsR[i].setNeutralMode(NeutralMode.Coast);
			//motorsL[i].configOpenloopRamp(0.2);
			motorsR[i].configPeakCurrentLimit(20);
			motorsR[i].configContinuousCurrentLimit(27);
			if (i > 0)
				motorsR[i].follow(motorsR[0]);
		}

		DifferentialDrive d = new DifferentialDrive(motorsL[0], motorsR[0]);
		d.setRightSideInverted(false);
		SendableRegistry.addChild(this, d);

		leftMotors = new SpeedControllerGroup(motorsL[0], motorsL[1]);
		rightMotors = new SpeedControllerGroup(motorsR[0], motorsR[1]);

		// The right-side drive encoder
		leftEncoder =
		new Encoder(DriveConstants.kLeftEncoderPorts[0], DriveConstants.kLeftEncoderPorts[1],
					DriveConstants.kLeftEncoderReversed);
		// The right-side drive encoder
		rightEncoder =
		new Encoder(DriveConstants.kRightEncoderPorts[0], DriveConstants.kRightEncoderPorts[1],
					DriveConstants.kRightEncoderReversed);
		
		 // Sets the distance per pulse for the encoders
		 leftEncoder.setDistancePerPulse(DriveConstants.kEncoderDistancePerPulse);
		 rightEncoder.setDistancePerPulse(DriveConstants.kEncoderDistancePerPulse);
		 odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
	 
		 resetEncoders();
		return d;
	}

	
		@Override
	public void periodic() {
		// Update the odometry in the periodic block
		odometry.update(Rotation2d.fromDegrees(getHeading()), leftEncoder.getDistance(),
						rightEncoder.getDistance());
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
		return new DifferentialDriveWheelSpeeds(leftEncoder.getRate(), rightEncoder.getRate());
	}

	/**
	 * Resets the odometry to the specified pose.
	 *
	 * @param pose The pose to which to set the odometry.
	 */
	public void resetOdometry(Pose2d pose) {
		resetEncoders();
		odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
	}

		/**
	 * Resets the drive encoders to currently read a position of 0.
	 */
	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}

		/**
	 * Gets the average distance of the two encoders.
	 *
	 * @return the average of the two encoder readings
	 */
	public double getAverageEncoderDistance() {
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;
	}

	/**
	 * Gets the left drive encoder.
	 *
	 * @return the left drive encoder
	 */
	public Encoder getLeftEncoder() {
		return leftEncoder;
	}

	/**
	 * Gets the right drive encoder.
	 *
	 * @return the right drive encoder
	 */
	public Encoder getRightEncoder() {
		return rightEncoder;
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

	public TalonSRXDrive withGyro(final Gyro gyro) {
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
		leftMotors.setVoltage(leftVolts);
		rightMotors.setVoltage(-rightVolts);
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
		//gyro.reset();
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
	public double getVelocity() {
		return (motorsL[0].getSelectedSensorVelocity() + motorsR[0].getSelectedSensorVelocity()) / 2;
	}

	@Override
	public double getVelocity(Side side) {
		return side == Side.RIGHT ? motorsR[0].getSelectedSensorVelocity() : motorsL[0].getSelectedSensorVelocity();
	}

	@Override
	public double getDistance() {
		return (motorsL[0].getSelectedSensorPosition() + motorsR[0].getSelectedSensorPosition()) / 2;
	}

	@Override
	public double getDistance(Side side) {
		return side == Side.RIGHT ? motorsR[0].getSelectedSensorPosition() : motorsL[0].getSelectedSensorPosition();
	}

	@Override
	public void resetSensors() {/*
		motorsL[0].setSelectedSensorPosition(0);
		motorsR[0].setSelectedSensorPosition(0);
		if (gyro != null)
			gyro.reset();*/
	}

	public boolean getIsReversed() {
		return reversedEntry.getBoolean(false);
	}

	public void setIsReversed(boolean reverse) {
		reversedEntry.setBoolean(reverse);
	}

	public void toggleIsReversed() {
		setIsReversed(!getIsReversed());;
	}

	public boolean getTankControls() {
		return tankControlsEntry.getBoolean(false);
	}

	public void setTankControls(boolean reverse) {
		tankControlsEntry.setBoolean(reverse);
	}

	public void toggleTankControls() {
		setTankControls(!getTankControls());;
	}

	public boolean getSafeMode() {
		return safeModeEntry.getBoolean(false);
	}

	public void setSafeMode(boolean enabled) {
		safeModeEntry.setBoolean(enabled);
		// update handled by listener in initSendable()
	}

	public void toggleSafeMode() {
		setSafeMode(!getSafeMode());
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
/*
		builder.addDoubleProperty("Left Velocity", () -> getVelocity(Side.LEFT), null);
		builder.addDoubleProperty("Right Velocity", () -> getVelocity(Side.RIGHT), null);
		builder.addDoubleProperty("Average Velocity", () -> getVelocity(), null);
		builder.addDoubleProperty("Average Distance", () -> getDistance(), null);
*/
		reversedEntry = builder.getEntry("Reverse Direction");
		reversedEntry.setDefaultBoolean(false);
		reversedEntry.setPersistent();

		tankControlsEntry = builder.getEntry("Tank Controls");
		tankControlsEntry.setDefaultBoolean(false);
		tankControlsEntry.setPersistent();

		safeModeEntry = builder.getEntry("Safe Mode");
		safeModeEntry.setDefaultBoolean(false);
		safeModeEntry.setPersistent();
		safeModeEntry.addListener(e -> {
			drive.setMaxOutput(e.value.getBoolean() ? SPEED_SAFE : SPEED_MAX);
			System.out.println("Safemode is " + (getSafeMode() ? "Enabled" : "Disabled") + ".");
		}, kNew | kUpdate | kLocal | kImmediate);

	}

}