package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class MecanumSubsystem extends SubsystemBase {
	public double wheelRadius = 3;
	protected final MecanumDrive drive;
	protected WPI_TalonSRX motorFL, motorFR, motorBL, motorBR;
	private Gyro gyro;

	Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
	Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
	Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
	Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);
	MecanumDriveKinematics m_kinematics;
	MecanumDriveOdometry m_odometry;

	public MecanumSubsystem(RobotBase robot) {
		super(robot);

		motorBL = new WPI_TalonSRX(2);
		motorFL = new WPI_TalonSRX(1);
		motorBR = new WPI_TalonSRX(4);
		motorFR = new WPI_TalonSRX(3);

		motorBL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		motorFL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		motorBR.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		motorFR.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		
		motorBR.setInverted(true);
		motorFR.setInverted(true);
		
		// Locations of the wheels relative to the robot center.

		// Creating my kinematics object using the wheel locations.
		m_kinematics = new MecanumDriveKinematics(m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);

		// Creating my odometry object from the kinematics object. Here,
		// our starting pose is 5 meters along the long end of the field and in the
		// center of the field along the short end, facing forward.
		m_odometry = new MecanumDriveOdometry(m_kinematics, new Rotation2d(gyro.getAngle()), new Pose2d(5.0, 13.5, new Rotation2d()));


		drive = new MecanumDrive(motorFL, motorBL, motorFR, motorBR);
		addChild("Mecanum Drive", drive);
	}

	public MecanumSubsystem useGyro(Gyro gyro){
		this.gyro = gyro;
		return this;
	}
	
	@Override
	public void periodic() {
		m_odometry.update(new Rotation2d(gyro.getAngle()), new MecanumDriveWheelSpeeds(
			convertToMetersPerSecond(motorFL.getSelectedSensorVelocity()),
			convertToMetersPerSecond(motorBL.getSelectedSensorVelocity()),
			convertToMetersPerSecond(motorFR.getSelectedSensorVelocity()),
			convertToMetersPerSecond(motorBR.getSelectedSensorVelocity())
		));
	}

	public Pose2d getPose() {
		return m_odometry.getPoseMeters();
	}
	
	public MecanumDrive getMecanumDrive() {
		return drive;
	}

	public double convertToEncoderUnits(double metersPerSecond){
		double c = 2 * Math.PI * wheelRadius; // wheel circumference
		double rps = metersPerSecond / c; // Rotations Per Second

		double EncoderUnits = rps * (4096 / 10);
		return EncoderUnits;
	}

	public double convertToMetersPerSecond(double EncoderUnits){
		double c = 2 * Math.PI * wheelRadius; // wheel circumference
		double rps = EncoderUnits * (10 / 4096);

		double metersPerSecond = rps * c;
		return metersPerSecond;
	}
}