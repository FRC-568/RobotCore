// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.chargedup;

import static frc.team568.robot.chargedup.Constants.SwerveConstants.kMaxSpeed;

import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.robot.subsystems.AprilTags;

class SwerveSubsystem extends SubsystemBase {
	private final Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
	private final Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
	private final Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
	private final Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);
	public boolean fieldRelative;

	private final SwerveModule m_frontLeft = new SwerveModule(1, 2, 1);
	private final SwerveModule m_frontRight = new SwerveModule(3, 4, 2);
	private final SwerveModule m_backLeft = new SwerveModule(5, 6, 3);
	private final SwerveModule m_backRight = new SwerveModule(7, 8, 4);

	private final Gyro m_gyro = new ADXRS450_Gyro();

	private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
			m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);

	// private final SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(m_kinematics, m_gyro.getRotation2d(),
	// 		new SwerveModulePosition[] {
	// 				m_frontLeft.getPosition(),
	// 				m_frontRight.getPosition(),
	// 				m_backLeft.getPosition(),
	// 				m_backRight.getPosition()
	// 		});

	// TODO: set relative cam pose to robot
	private final AprilTags apriltag = new AprilTags("photonvision", new Translation3d(0.0, 0.0, 0.0), new Rotation3d(0.0, 0.0, 0.0));

	private final SwerveDrivePoseEstimator m_estimator;

	public SwerveSubsystem(Pose2d startingPose) {
		m_gyro.reset();
		m_estimator = new SwerveDrivePoseEstimator(m_kinematics, m_gyro.getRotation2d(),
				new SwerveModulePosition[] {
					m_frontLeft.getPosition(),
					m_frontRight.getPosition(),
					m_backLeft.getPosition(),
					m_backRight.getPosition()
				},
				startingPose);
	}

	/**
	 * Method to drive the robot using joystick info.
	 *
	 * @param xSpeed        Speed of the robot in the x direction (forward).
	 * @param ySpeed        Speed of the robot in the y direction (sideways).
	 * @param rot           Angular rate of the robot.
	 * @param fieldRelative Whether the provided x and y speeds are relative to the
	 *                      field.
	 */
	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		var swerveModuleStates = m_kinematics.toSwerveModuleStates(
				fieldRelative
						? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, m_gyro.getRotation2d())
						: new ChassisSpeeds(xSpeed, ySpeed, rot));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
		m_frontLeft.setDesiredState(swerveModuleStates[0]);
		m_frontRight.setDesiredState(swerveModuleStates[1]);
		m_backLeft.setDesiredState(swerveModuleStates[2]);
		m_backRight.setDesiredState(swerveModuleStates[3]);
	}

	/** Updates the field relative position of the robot. */
	public void updateOdometry() {
		m_estimator.update(
				m_gyro.getRotation2d(),
				new SwerveModulePosition[] {
						m_frontLeft.getPosition(),
						m_frontRight.getPosition(),
						m_backLeft.getPosition(),
						m_backRight.getPosition() });
		EstimatedRobotPose camPose = apriltag.getEstimatedPose().get();
		m_estimator.addVisionMeasurement(camPose.estimatedPose.toPose2d(), camPose.timestampSeconds);
	}

	@Override
	public void periodic() {
		updateOdometry();
	}
	
}
