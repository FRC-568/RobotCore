// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.crescendo;

import static frc.team568.robot.crescendo.Constants.SwerveConstants.kMaxSpeed;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.SwerveModule;

class SwerveSubsystem extends SubsystemBase {
	protected static final String FIELD_REL_KEY = "Field Relative";

	private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();

	//private GenericEntry[] cancoderOffsets;
	//private double[] cancoderPrevOffsets;

	private Translation2d targetTrajectory = new Translation2d(0, 0);
	private Rotation2d targetRotation = new Rotation2d(0);

	private boolean _fieldRelative = Preferences.getBoolean(FIELD_REL_KEY, false);

	protected SwerveDrive drive;

	private boolean slowMode = false;
	private double slowMultiplier = 0.25;
	private double normalMultiplier = 1.0;
	private double speedMultiplier = 1.0;

	public SwerveSubsystem(Pose2d startingPose) {
		try {
			drive = new SwerveParser(new File(Filesystem.getDeployDirectory(),"swerve")).createSwerveDrive(kMaxSpeed);
		} catch(IOException e) {
			DriverStation.reportError(e.getMessage(),e.getStackTrace());
			throw new RuntimeException("Cannot create SwerveSubsystem: cannot read config files.");
		}
	}

	/**
	 * Method to drive the robot using joystick info.
	 *
	 * @param xSpeed Speed of the robot in the x direction (forward).
	 * @param ySpeed Speed of the robot in the y direction (sideways).
	 * @param rot    Angular rate of the robot.
	 */
	public void drive(double xSpeed, double ySpeed, double rot) {
		drive(xSpeed, ySpeed, rot, isFieldRelative());
	}

	/**
	 * Method to drive the robot using joystick info.
	 *
	 * @param xSpeed        Speed of the robot in meters / second along the x direction (forward).
	 * @param ySpeed        Speed of the robot in meters / second along the y direction (sideways).
	 * @param rot           Angular rate of the robot in radians (counter-clockwise is positive.)
	 * @param fieldRelative Override setting for field relative controls
	 */
	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		xSpeed = xSpeed * speedMultiplier;
		ySpeed = ySpeed * speedMultiplier;
		setModuleStates(fieldRelative
				? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, m_gyro.getRotation2d())
				: new ChassisSpeeds(xSpeed, ySpeed, rot));
	}

	public void setModuleStates(ChassisSpeeds chassisSpeeds) {
		drive.setChassisSpeeds(chassisSpeeds);
	}

	public ChassisSpeeds getChassisSpeeds() {
		return drive.kinematics.toChassisSpeeds(getModuleStates());
	}

	public void toggleSlowMode() {
		slowMode = !slowMode;
		if (slowMode) {
			speedMultiplier = slowMultiplier;
		} else {
			speedMultiplier = normalMultiplier;
		}
	}

	public Translation2d getTargetTrajectory(){
		return targetTrajectory;
	}

	public Rotation2d getTargetRotation(){
		return targetRotation;
	}

	public void setModuleStates(SwerveModuleState[] swerveModuleStates) {
		setModuleStates(swerveModuleStates, true);
	}

	public void setModuleStates(SwerveModuleState[] swerveModuleStates, boolean isOpenLoop) {
		if (swerveModuleStates == null || swerveModuleStates.length < drive.getModules().length)
			throw new RuntimeException("Invalid SwerveModuleState size");

		drive.setModuleStates(swerveModuleStates, isOpenLoop);
	}


	protected SwerveModule[] getModules() {
		return drive.getModules();
	}

	public SwerveModuleState[] getModuleStates() {
		return drive.getStates();
	}

	protected SwerveModulePosition[] getModulePositions() {
		return drive.getModulePositions();
	}
/* 
	public Rotation2d getHeading() {
		return m_gyro.getRotation2d();
	}

	/*public void resetHeading() {
		m_gyro.reset();
	}
*/
	public SwerveDriveKinematics getKinematics() {
		return drive.kinematics;
	}

	public Pose2d getPose() {
		return drive.getPose();
	}

	public void resetPose(Pose2d pose) {
		drive.resetOdometry(pose);
	}

	public boolean isFieldRelative() {
		return _fieldRelative;
	}

	public void setFieldRelative(boolean isEnabled) {
		_fieldRelative = isEnabled;
		Preferences.setBoolean(FIELD_REL_KEY, _fieldRelative);
	}

	public boolean toggleFieldRelative() {
		boolean fr = !isFieldRelative();
		setFieldRelative(fr);
		return fr;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty(FIELD_REL_KEY, this::isFieldRelative, this::setFieldRelative);
	}

}
