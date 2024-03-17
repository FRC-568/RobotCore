// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveParser;
import swervelib.SwerveModule;

public class SwerveSubsystem extends SubsystemBase {
	protected static final String FIELD_REL_KEY = "Field Relative";
	private boolean _fieldRelative = Preferences.getBoolean(FIELD_REL_KEY, false);

	protected static final String SLOW_MODE_KEY = "Slow Mode";
	private double slowMultiplier = 0.25, normalMultiplier = 1.0;
	private boolean _slowMode = Preferences.getBoolean(SLOW_MODE_KEY, false);
	private double speedMultiplier = _slowMode ? slowMultiplier : normalMultiplier;

	public final SwerveDrive drive;

	public SwerveSubsystem(File configDirectory, double maxSpeedMPS) {
		try {
			this.drive = new SwerveParser(configDirectory).createSwerveDrive(maxSpeedMPS);
		} catch (IOException e) {
			DriverStation.reportError(e.getMessage(), e.getStackTrace());
			throw new RuntimeException("Cannot create SwerveSubsystem: cannot read config files.");
		}
	}

	public SwerveSubsystem(String configDirectoryPath, double maxSpeedMPS) {
		this(new File(Filesystem.getDeployDirectory(), configDirectoryPath), maxSpeedMPS);
	}

	public SwerveSubsystem(SwerveDriveConfiguration config, SwerveControllerConfiguration controllerConfig, double maxSpeedMPS) {
		this.drive = new SwerveDrive(config, controllerConfig, maxSpeedMPS);
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
	 * @param xSpeed        Speed of the robot in meters / second along the x
	 *                      direction (forward).
	 * @param ySpeed        Speed of the robot in meters / second along the y
	 *                      direction (sideways).
	 * @param rot           Angular rate of the robot in radians (counter-clockwise
	 *                      is positive.)
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

	public void addVisionMeasurement(Pose2d robotPose, double timestamp) {
		drive.addVisionMeasurement(robotPose, timestamp);
	}

	public void addVisionMeasurement(Pose2d robotPose, double timestamp, Matrix<N3, N1> standardDeviations) {
		drive.addVisionMeasurement(robotPose, timestamp, standardDeviations);
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
		speedMultiplier = _slowMode ? slowMultiplier : normalMultiplier;
		Preferences.setBoolean(SLOW_MODE_KEY, _slowMode);
		System.out.printf("%s controls activated. (%d%%)\n", _slowMode ? "Slow" : "Fast", speedMultiplier * 100);
	}

	public void setSpeedMultipliers(double slowMultiplier, double normalMultiplier) {
		this.slowMultiplier = slowMultiplier;
		this.normalMultiplier = normalMultiplier;
		if (isSlowModeActive())
			speedMultiplier = slowMultiplier;
		else
			speedMultiplier = normalMultiplier;
	}

	public boolean toggleSlowMode() {
		boolean sm = !isSlowModeActive();
		setSlowMode(sm);
		return sm;
	}

	public void initDefaultCommand(
			final DoubleSupplier swerveForward,
			final DoubleSupplier swerveLeft,
			final DoubleSupplier swerveCCW) {
		setDefaultCommand(new RunCommand(
				() -> drive(
					swerveForward.getAsDouble(),
					swerveLeft.getAsDouble(),
					swerveCCW.getAsDouble()),
				this));
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty(FIELD_REL_KEY, this::isFieldRelative, this::setFieldRelative);
		builder.addBooleanProperty(SLOW_MODE_KEY, this::isSlowModeActive, this::setSlowMode);
	}
}
