package frc.team568.robot.rapidreact;

import static frc.team568.robot.rapidreact.Config.MecanumSubsystem.kMotorID_BL;
import static frc.team568.robot.rapidreact.Config.MecanumSubsystem.kMotorId_BR;
import static frc.team568.robot.rapidreact.Config.MecanumSubsystem.kMotorId_FL;
import static frc.team568.robot.rapidreact.Config.MecanumSubsystem.kMotorId_FR;
import static frc.team568.util.Utilities.clamp;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.kinematics.MecanumDriveWheelPositions;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MecanumSubsystem extends SubsystemBase {
	public static final int ENCODER_RESOLUTION = 4096;
	public static double MAX_VELOCITY = 2500;
	public double wheelRadius = 3;
	protected final MecanumDrive drive;
	protected WPI_TalonSRX motorFL, motorFR, motorBL, motorBR;
	private ADXRS450_Gyro gyro;

	// Locations of the wheels relative to the robot center.
	Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
	Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
	Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
	Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);

	MecanumDriveKinematics m_kinematics;
	MecanumDriveOdometry m_odometry;

	public MecanumSubsystem(ADXRS450_Gyro gyro) {
		this.gyro = gyro;
		motorBL = new WPI_TalonSRX(kMotorID_BL);
		motorFL = new WPI_TalonSRX(kMotorId_FL);
		motorBR = new WPI_TalonSRX(kMotorId_BR);
		motorFR = new WPI_TalonSRX(kMotorId_FR);

		setupMotor(motorBL, false);
		setupMotor(motorFL, false);
		setupMotor(motorBR, true);
		setupMotor(motorFR, true);		

		// Creating my kinematics object using the wheel locations.
		m_kinematics = new MecanumDriveKinematics(m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);

		// Creating my odometry object from the kinematics object. Here,
		// our starting pose is 5 meters along the long end of the field and in the
		// center of the field along the short end, facing forward.
		var heading = Rotation2d.fromDegrees(gyro.getAngle());
		m_odometry = new MecanumDriveOdometry(
			m_kinematics,
			heading,
			new MecanumDriveWheelPositions(),
			new Pose2d(1, 4, heading));

		drive = new MecanumDrive(motorFL, motorBL, motorFR, motorBR);

		addChild("Mecanum Drive", drive);
	}

	private void setupMotor(TalonSRX motor, Boolean isInverted){
		// motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		// motor.setSensorPhase(true);

		// motor.config_kP(0, 0.25 , 30);
		// motor.config_kI(0, 0.002, 30);
		// motor.config_kD(0, 10   , 30);
		
		motor.setInverted(isInverted);
		
		//remember to set back to 0
		motor.configClosedloopRamp(0.1);
		motor.configOpenloopRamp(0.1);
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}
	
	@Override
	public void periodic() {
		m_odometry.update(Rotation2d.fromDegrees(gyro.getAngle()), new MecanumDriveWheelPositions(
			rawToMeters(motorFL.getSelectedSensorPosition()),
			rawToMeters(motorBL.getSelectedSensorPosition()),
			rawToMeters(motorFR.getSelectedSensorPosition()),
			rawToMeters(motorBR.getSelectedSensorPosition())
		));
	}

	public Pose2d getPose() {
		return m_odometry.getPoseMeters();
	}
	
	public MecanumDrive getMecanumDrive() {
		return drive;
	}

	public double convertToEncoderUnits(double metersPerSecond){
		return metersPerSecond / wheelCircumference() * (ENCODER_RESOLUTION / 10);
	}

	public double convertToMetersPerSecond(double encoderUnits){
		return encoderUnits * (10 / ENCODER_RESOLUTION) * wheelCircumference();
	}

	public double rawToMeters(double rawEncoderTicks) {
		return wheelCircumference() * ENCODER_RESOLUTION;
	}

	public double wheelCircumference() {
		return 2 * Math.PI * wheelRadius;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("FL Speed", motorFL::getSelectedSensorVelocity, null);
		builder.addDoubleProperty("BL Speed", motorBL::getSelectedSensorVelocity, null);
		builder.addDoubleProperty("FR Speed", motorFR::getSelectedSensorVelocity, null);
		builder.addDoubleProperty("BR Speed", motorBR::getSelectedSensorVelocity, null);
	}
	
	protected MotorController makeWrapper(final WPI_TalonSRX controller, double maxSpeed) {
		return new MotorController() {
			@Override
			public double get() {
				return clamp(controller.getSelectedSensorVelocity() / maxSpeed, -1.0, 1.0);
			}

			@Override
			public void set(double speed) {
				controller.set(ControlMode.Velocity, speed * maxSpeed);
			}

			@Override
			public void setInverted(boolean isInverted) {
				controller.setInverted(isInverted);
			}

			@Override
			public boolean getInverted() {
				return controller.getInverted();
			}

			@Override
			public void disable() {
				controller.disable();
			}

			@Override
			public void stopMotor() {
				controller.stopMotor();
			}
		};
	}

	public void driveStraight(double d) {
		getMecanumDrive().driveCartesian(d, 0, 0);
	}

	public void stop(){
		getMecanumDrive().driveCartesian(0, 0, 0);
	}
	
}