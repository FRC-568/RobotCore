// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.chargedup;

import static frc.team568.robot.chargedup.Constants.SwerveConstants.kBackOffset;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kBackRot;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kDrivePidChannel;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kFrontOffset;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kFrontRot;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kLeftOffset;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kLeftRot;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kMaxSpinRate;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kRightOffset;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kRightRot;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class SwerveSubsystem extends SubsystemBase {
	protected static final String FIELD_REL_KEY = "Field Relative Control";

	private final SwerveModule[] m_modules;
	private final SwerveDriveKinematics m_kinematics;
	private final Gyro m_gyro = new ADXRS450_Gyro();

	private boolean fieldRelativeControl;

	private double kTurnP, kTurnI, kTurnD, kDriveP, kDriveI, kDriveD;

	// TODO: set relative cam pose to robot
	// private final AprilTags apriltag = new AprilTags("photonvision", new Translation3d(0.0, 0.0, 0.0),
	// 		new Rotation3d(0.0, 0.0, 0.0));

	private final SwerveDrivePoseEstimator m_estimator;

	public SwerveSubsystem(Pose2d startingPose) {
		m_modules = new SwerveModule[] {
			new SwerveModule(1, 2, 1, new Translation2d(kFrontOffset, 0), kFrontRot),
			new SwerveModule(3, 4, 2, new Translation2d(0, kLeftOffset), kLeftRot),
			new SwerveModule(5, 6, 3, new Translation2d(0, -kRightOffset), kRightRot),
			new SwerveModule(7, 8, 4, new Translation2d(-kBackOffset, 0), kBackRot)
		};
		m_kinematics = new SwerveDriveKinematics(getModuleLocations());
		m_estimator = new SwerveDrivePoseEstimator(m_kinematics, getHeading(), getModulePositions(), startingPose);

		fieldRelativeControl = Preferences.getBoolean(FIELD_REL_KEY, true);

		var turnPid = m_modules[0].m_turningPIDController;
		kTurnP = turnPid.getP();
		kTurnI = turnPid.getI();
		kTurnD = turnPid.getD();

		var drivePid = m_modules[0].m_drivePIDController;
		kDriveP = drivePid.getP(kDrivePidChannel);
		kDriveI = drivePid.getI(kDrivePidChannel);
		kDriveD = drivePid.getD(kDrivePidChannel);
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
		xSpeed *= kMaxSpeed;
		ySpeed *= kMaxSpeed;
		rot *= kMaxSpinRate;
		
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
		// Optional<EstimatedRobotPose> camPose = apriltag.getEstimatedPose();
		// if (camPose.isPresent()) {
		// 	m_estimator.addVisionMeasurement(camPose.get().estimatedPose.toPose2d(), camPose.get().timestampSeconds);
		// }
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

	void setTurnP(double turnP) {
		kTurnP = turnP;
		for (int i = 0; i < m_modules.length; i++) {
			m_modules[i].m_turningPIDController.setP(turnP);
			m_modules[i].m_driveMotor.burnFlash();
		}
	}

	void setTurnI(double turnI) {
		kTurnI = turnI;
		for (int i = 0; i < m_modules.length; i++) {
			m_modules[i].m_turningPIDController.setI(turnI);
			m_modules[i].m_driveMotor.burnFlash();
		}
	}

	void setTurnD(double turnD) {
		kTurnD = turnD;
		for (int i = 0; i < m_modules.length; i++) {
			m_modules[i].m_turningPIDController.setD(turnD);
			m_modules[i].m_driveMotor.burnFlash();
		}
	}

	void setDriveP(double driveP) {
		for (int i = 0; i < m_modules.length; i++) {
			m_modules[i].m_drivePIDController.setP(driveP, kDrivePidChannel);
			m_modules[i].m_driveMotor.burnFlash();
		}
		kDriveP = driveP;
	}

	void setDriveI(double driveI) {
		kDriveI = driveI;
		for (int i = 0; i < m_modules.length; i++) {
			m_modules[i].m_drivePIDController.setI(driveI, kDrivePidChannel);
			m_modules[i].m_driveMotor.burnFlash();
		}
	}

	void setDriveD(double driveD) {
		kDriveD = driveD;
		for (int i = 0; i < m_modules.length; i++) {
			m_modules[i].m_drivePIDController.setD(driveD, kDrivePidChannel);
			m_modules[i].m_driveMotor.burnFlash();
		}
	}

	public double getDriveP() {
		return kDriveP;
	}

	public double getDriveI() {
		return kDriveI;
	}

	public double getDriveD() {
		return kDriveD;
	}

	public double getTurnP() {
		return kTurnP;
	}

	public double getTurnI() {
		return kTurnI;
	}

	public double getTurnD() {
		return kTurnD;
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty(FIELD_REL_KEY, this::isControlFieldRelative, this::setControlFieldRelative);
		builder.addDoubleProperty("turn-p", this::getTurnP, this::setTurnP);
		builder.addDoubleProperty("turn-i", this::getTurnI, this::setTurnI);
		builder.addDoubleProperty("turn-d", this::getTurnD, this::setTurnD);
		builder.addDoubleProperty("drive-p", this::getDriveP, this::setDriveP);
		builder.addDoubleProperty("drive-i", this::getDriveI, this::setDriveI);
		builder.addDoubleProperty("drive-d", this::getDriveD, this::setDriveD);

	}

}
