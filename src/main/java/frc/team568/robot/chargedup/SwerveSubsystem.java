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
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class SwerveSubsystem extends SubsystemBase {
	protected static final String FIELD_REL_KEY = "Field Relative";

	private final SwerveModule[] m_modules;
	private final SwerveDriveKinematics m_kinematics;
	private final Gyro m_gyro = new ADXRS450_Gyro();

	final ShuffleboardTab configTab;

	private GenericEntry[] cancoderOffsets;
	private double[] cancoderPrevOffsets;

	private GenericEntry _fieldRelative;

	PIDConfig drivePID, turnPID;

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

		configTab = setupConfigTab();

		for (ModuleIndex ind : ModuleIndex.values())
			addChild(ind.name() + " module", m_modules[ind.index]);
		addChild("Gyro", (ADXRS450_Gyro)m_gyro);
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

	public boolean isFieldRelative() {
		return _fieldRelative.getBoolean(true);
	}

	public void setFieldRelative(boolean isEnabled) {
		_fieldRelative.setBoolean(isEnabled);
	}

	public boolean toggleFieldRelative() {
		boolean fr = !isFieldRelative();
		setFieldRelative(fr);
		return fr;
	}

	private void updateConfig() {
		if (drivePID.isDirty()) {
			for (var module : m_modules) {
				var controller = module.m_drivePIDController;
				controller.setP(drivePID.getP(), kDrivePidChannel);
				controller.setI(drivePID.getI(), kDrivePidChannel);
				controller.setD(drivePID.getD(), kDrivePidChannel);
				module.m_driveMotor.burnFlash();
			}
			drivePID.clearDirtyFlag();
		}

		if (turnPID.isDirty()) {
			for (var module : m_modules) {
				var controller = module.m_turningPIDController;
				controller.setP(turnPID.getP());
				controller.setI(turnPID.getI());
				controller.setD(turnPID.getD());
			}
			turnPID.clearDirtyFlag();
		}

		for (int i = 0; i < cancoderOffsets.length; i++) {
			var newValue = cancoderOffsets[i].get().getDouble();
			if (newValue != cancoderPrevOffsets[i]) {
				m_modules[i].m_turningEncoder.configMagnetOffset(newValue);
				m_modules[i].m_turningEncoder.setPositionToAbsolute();
				cancoderPrevOffsets[i] = newValue;
			}
		}
	}

	@Override
	public void periodic() {
		updateConfig();
		updatePose();
	}

	private ShuffleboardTab setupConfigTab() {
		ShuffleboardTab configTab = Shuffleboard.getTab("Swerve Config");
		ShuffleboardLayout layout;
		double kP, kI, kD;
		GenericEntry entryP, entryI, entryD;

		// Add drive motor settings to their own layout
		layout = configTab.getLayout("Drive", BuiltInLayouts.kList);

		var driveController = m_modules[0].m_drivePIDController;
		kP = driveController.getP(kDrivePidChannel);
		kI = driveController.getI(kDrivePidChannel);
		kD = driveController.getD(kDrivePidChannel);

		entryP = layout.addPersistent("kP", kP).getEntry();
		entryI = layout.addPersistent("kI", kI).getEntry();
		entryD = layout.addPersistent("kD", kD).getEntry();

		this.drivePID = new PIDConfig(entryP, entryI, entryD);

		// Add turning motor settings to their own layout
		layout = configTab.getLayout("Turn", BuiltInLayouts.kList);

		var turnController = m_modules[0].m_turningPIDController;
		kP = turnController.getP();
		kI = turnController.getI();
		kD = turnController.getD();

		entryP = layout.addPersistent("kP", kP).getEntry();
		entryI = layout.addPersistent("kI", kI).getEntry();
		entryD = layout.addPersistent("kD", kD).getEntry();

		this.turnPID = new PIDConfig(entryP, entryI, entryD);

		// Field-relative controls
		_fieldRelative = configTab.addPersistent(FIELD_REL_KEY, true).getEntry();

		// Add section for CANCoder settings
		layout = configTab.getLayout("CANCoders", BuiltInLayouts.kList);

		cancoderOffsets = new GenericEntry[m_modules.length];
		cancoderPrevOffsets = new double[m_modules.length];

		for (int i = 0; i < m_modules.length; i++) {
			double encValue = m_modules[i].m_turningEncoder.configGetMagnetOffset();
			String modName = ModuleIndex.byIndex(i).name();
			cancoderOffsets[i] = layout.addPersistent(modName + " Offset", encValue).getEntry();
			cancoderPrevOffsets[i] = encValue;
		}

		return configTab;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty(FIELD_REL_KEY, this::isFieldRelative, this::setFieldRelative);
	}

	public final class PIDConfig {
		private final GenericEntry entryP, entryI, entryD;
		private double lastP = -1, lastI = -1, lastD = -1;

		private PIDConfig(final GenericEntry p, final GenericEntry i, final GenericEntry d) {
			this.entryP = p;
			this.entryI = i;
			this.entryD = d;
		}

		public double getP() {
			return entryP.get().getDouble();
		}

		public boolean setP(double value) {
			if (value == getP())
				return false;

			entryP.setDouble(value);
			return true;
		}

		public double getI() {
			return entryI.get().getDouble();
		}

		public boolean setI(double value) {
			if (value == getI())
				return false;

			entryI.setDouble(value);
			return true;
		}

		public double getD() {
			return entryD.get().getDouble();
		}

		public boolean setD(double value) {
			if (value == getD())
				return false;

			entryD.setDouble(value);
			return true;
		}

		private boolean isDirty() {
			return lastP != getP() || lastI != getI() || lastD != getD();
		}

		private void clearDirtyFlag() {
			lastP = getP();
			lastI = getI();
			lastD = getD();
		}
	}

	public enum ModuleIndex {
		FRONT(0),
		LEFT(1),
		RIGHT(2),
		BACK(3);

		public final int index;

		ModuleIndex(int index) {
			this.index = index;
		}

		public static ModuleIndex byIndex(int indexValue) {
			for (var m : values()) {
				if (m.index == indexValue)
					return m;
			}
			return null;
		}
	}

}
