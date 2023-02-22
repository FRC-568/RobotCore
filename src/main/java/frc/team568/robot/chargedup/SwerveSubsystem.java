// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.chargedup;

import static frc.team568.robot.chargedup.Constants.SwerveConstants.*;

import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.robot.subsystems.AprilTags;

class SwerveSubsystem extends SubsystemBase {
	protected static final String FIELD_REL_KEY = "Field Relative Control";

	private final SwerveModule[] m_modules;
	private final SwerveDriveKinematics m_kinematics;
	private final Gyro m_gyro = new ADXRS450_Gyro();

	private boolean fieldRelativeControl;

	// TODO: set relative cam pose to robot
	private final AprilTags apriltag = new AprilTags("photonvision", new Translation3d(0.0, 0.0, 0.0),
			new Rotation3d(0.0, 0.0, 0.0));

	private final SwerveDrivePoseEstimator m_estimator;

	public SwerveSubsystem(Pose2d startingPose) {
		m_modules = new SwerveModule[] {
			new SwerveModule(1, 2, 1, new Translation2d(kFrontOffset, 0)),
			new SwerveModule(3, 4, 2, new Translation2d(0, kLeftOffset)),
			new SwerveModule(5, 6, 3, new Translation2d(0, kRightOffset)),
			new SwerveModule(7, 8, 4, new Translation2d(-kBackOffset, 0))
		};
		m_kinematics = new SwerveDriveKinematics(getModuleLocations());
		m_estimator = new SwerveDrivePoseEstimator(m_kinematics, getHeading(), getModulePositions(), startingPose);

		fieldRelativeControl = Preferences.getBoolean(FIELD_REL_KEY, true);
	}

	/**
	 * Method to drive the robot using joystick info.
	 *
	 * @param xSpeed        Speed of the robot in the x direction (forward).
	 * @param ySpeed        Speed of the robot in the y direction (sideways).
	 * @param rot           Angular rate of the robot.
	 */
	public void drive(double xSpeed, double ySpeed, double rot) {
		drive(xSpeed, ySpeed, rot, isControlFieldRelative());
	}

	/**
	 * Method to drive the robot using joystick info.
	 *
	 * @param xSpeed        Speed of the robot in the x direction (forward).
	 * @param ySpeed        Speed of the robot in the y direction (sideways).
	 * @param rot           Angular rate of the robot.
	 * @param fieldRelative Override setting for field relative controls
	 */
	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		setModuleStates(fieldRelative
				? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, m_gyro.getRotation2d())
				: new ChassisSpeeds(xSpeed, ySpeed, rot));
	}
	
	public void setModuleStates(ChassisSpeeds outChassisSpeeds) {
		var swerveModuleStates = m_kinematics.toSwerveModuleStates(outChassisSpeeds);
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
		setModuleStates(swerveModuleStates);
	}

	public void setModuleStates(SwerveModuleState[] swerveModuleStates) {
		if (swerveModuleStates == null || swerveModuleStates.length < m_modules.length)
			throw new RuntimeException("Invalid SwerveModuleState size");

		for (int i = 0; i < m_modules.length; i++)
			m_modules[i].setDesiredState(swerveModuleStates[i]);
	}

	protected SwerveModule[] getModules() {
		return m_modules.clone();
	}

	protected SwerveModulePosition[] getModulePositions() {
		var positions = new SwerveModulePosition[m_modules.length];
		for (int i = 0; i < m_modules.length; i++)
			positions[i] = m_modules[i].getPosition();
		return positions;
	}

	protected Translation2d[] getModuleLocations() {
		var locations = new Translation2d[m_modules.length];
		for (int i = 0; i < m_modules.length; i++)
			locations[i] = m_modules[i].location;
		return locations;
	}

	public Rotation2d getHeading() {
		return m_gyro.getRotation2d();
	}

	public void resetHeading() {
		m_gyro.reset();
	}

	public SwerveDriveKinematics getKinematics() {
		return m_kinematics;
	}

	public Pose2d getPose() {
		return m_estimator.getEstimatedPosition();
	}

	public void resetPose(Pose2d pose) {
		m_estimator.resetPosition(getHeading(), getModulePositions(), pose);
	}

	/** Updates the field relative position of the robot. */
	public void updatePose() {
		m_estimator.update(m_gyro.getRotation2d(), getModulePositions());
		EstimatedRobotPose camPose = apriltag.getEstimatedPose().get();
		m_estimator.addVisionMeasurement(camPose.estimatedPose.toPose2d(), camPose.timestampSeconds);
	}

	public boolean isControlFieldRelative() {
		return fieldRelativeControl;
	}

	public void setControlFieldRelative(boolean isEnabled) {
		fieldRelativeControl = isEnabled;
		Preferences.setBoolean(FIELD_REL_KEY, isEnabled);
	}

	public boolean toggleControlFieldRelative() {
		var fr = !isControlFieldRelative();
		setControlFieldRelative(fr);
		return fr;
	}

	@Override
	public void periodic() {
		updatePose();
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty(FIELD_REL_KEY, this::isControlFieldRelative, this::setControlFieldRelative);
	}

}
