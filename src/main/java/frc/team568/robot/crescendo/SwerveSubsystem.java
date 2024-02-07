// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.crescendo;

import static frc.team568.robot.crescendo.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.crescendo.Constants.SwerveConstants.kNormalMultiplier;
import static frc.team568.robot.crescendo.Constants.SwerveConstants.kSlowMultiplier;

import java.io.File;
import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.SwerveModule;

class SwerveSubsystem extends SubsystemBase {
	protected static final String FIELD_REL_KEY = "Field Relative";
	private boolean _fieldRelative = Preferences.getBoolean(FIELD_REL_KEY, false);

	protected static final String SLOW_MODE_KEY = "Slow Mode";
	private boolean _slowMode = Preferences.getBoolean(SLOW_MODE_KEY, false);
	private double speedMultiplier = _slowMode ? kSlowMultiplier : kNormalMultiplier;

	protected SwerveDrive drive;

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
		var translation = new Translation2d(xSpeed * speedMultiplier, ySpeed * speedMultiplier);
		drive.drive(translation, rot, fieldRelative, false);
	}

	public void setModuleStates(ChassisSpeeds chassisSpeeds) {
		drive.setChassisSpeeds(chassisSpeeds);
	}

	public ChassisSpeeds getChassisSpeeds() {
		return drive.kinematics.toChassisSpeeds(getModuleStates());
	}

	public void setModuleStates(SwerveModuleState[] swerveModuleStates) {
		setModuleStates(swerveModuleStates, false);
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

	public SwerveDriveKinematics getKinematics() {
		return drive.kinematics;
	}

	public Pose2d getPose() {
		return drive.getPose();
	}

	public void resetPose(Pose2d pose) {
		drive.resetOdometry(pose);
	}

	public double getTravelSpeedMS() {
		var cs = getChassisSpeeds();
		return new Translation2d(cs.vxMetersPerSecond, cs.vyMetersPerSecond).getNorm();
	}

	public double getTravelBearingDeg() {
		var cs = getChassisSpeeds();
		return new Translation2d(cs.vxMetersPerSecond, cs.vyMetersPerSecond).getAngle().getDegrees();
	}

	public double getHeadingDeg() {
		return drive.getYaw().getDegrees();
	}

	public void configurePathplanner() {
		AutoBuilder.configureHolonomic(
				this::getPose, // Pose2d supplier
				this::resetPose, // Pose2d consumer, used to reset odometry at the beginning of auto
				this::getChassisSpeeds,
				this::setModuleStates, // SwerveDriveKinematics
				new HolonomicPathFollowerConfig(
					new PIDConstants(0.05, 0.0, 0.05), // PID constants to correct for translation error (used to create the X and Y PID controllers)
					new PIDConstants(0.05, 0.0, 0.0),
					1.0,
					0.39,
					new ReplanningConfig(true, true, 1, 1)), // PID constants to correct for rotation error (used to create the rotation controller)
				() -> {
					var alliance = DriverStation.getAlliance();
					if (alliance.isPresent()) {
						return alliance.get() == DriverStation.Alliance.Red;
					}
					return false;
				},
				this
			);
	}

	public boolean isFieldRelative() {
		return _fieldRelative;
	}

	public void setFieldRelative(boolean isEnabled) {
		_fieldRelative = isEnabled;
		Preferences.setBoolean(FIELD_REL_KEY, _fieldRelative);
		System.out.printf("Field relative control is %s.\n", _fieldRelative ? "enabled" : "disabled");
	}

	public boolean toggleFieldRelative() {
		boolean fr = !isFieldRelative();
		setFieldRelative(fr);
		return fr;
	}

	public boolean isSlowModeActive() {
		return _slowMode;
	}

	public void setSlowMode(boolean isEnabled) {
		_slowMode = isEnabled;
		speedMultiplier = _slowMode ? kSlowMultiplier : kNormalMultiplier;
		Preferences.setBoolean(SLOW_MODE_KEY, _slowMode);
		System.out.printf("%s controls activated. (%d%%)\n", _slowMode ? "Slow" : "Fast", speedMultiplier * 100);
	}

	public boolean toggleSlowMode() {
		boolean sm = !isSlowModeActive();
		setSlowMode(sm);
		return sm;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty(FIELD_REL_KEY, this::isFieldRelative, this::setFieldRelative);
		builder.addBooleanProperty(SLOW_MODE_KEY, this::isSlowModeActive, this::setSlowMode);
	}

}
