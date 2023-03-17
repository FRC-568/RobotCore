// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.chargedup;

import static frc.team568.robot.chargedup.Constants.SwerveConstants.kEncoderResolution;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kMaxRampRate;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kWheelCircumference;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kDrivePidChannel;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kRelativeEncoderResolution;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kDriveGearRatio;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

public class SwerveModule implements Sendable {
	private DoubleSupplier drivePosition;
	private DoubleSupplier driveVelocity;
	private DoubleSupplier turningAngle;

	final CANSparkMax m_driveMotor;
	private final CANSparkMax m_turningMotor;
	final CANCoder m_turningEncoder;

	// WARNING: CHANGE THIS BACK TO PRIVATE
	public final RelativeEncoder m_driveEncoder;
	final SparkMaxPIDController m_drivePIDController;
	private double m_driveFeedforward = 0.088;

	// Gains are for example purposes only - must be determined for your own robot!
	public PIDController m_turningPIDController = new PIDController(12, 0, 0);
	private SimpleMotorFeedforward m_turnFeedforward = new SimpleMotorFeedforward(0.05, 0.1);

	/**
	 * The module's location in meters from the center of rotation.
	 */
	public Translation2d location;

	/**
	 * Constructs a SwerveModule with a drive motor, turning motor, drive encoder
	 * and turning encoder.
	 *
	 * @param driveMotorChannel     CAN channel for the drive motor.
	 * @param turningMotorChannel   CAN channel for the turning motor.
	 * @param turningEncoderChannel CAN channel for the turning encoder.
	 * @param location              Location of the module in meters relative to the robot's center of rotation.
	 */
	public SwerveModule(
			int driveMotorChannel,
			int turningMotorChannel,
			int turningEncoderChannel,
			Translation2d location,
			double turnOffset) {

		// Setup Drive Motor
		m_driveMotor = new CANSparkMax(driveMotorChannel, MotorType.kBrushless);
		m_driveMotor.setIdleMode(IdleMode.kBrake);
		m_driveMotor.setClosedLoopRampRate(kMaxRampRate);

		m_driveEncoder = m_driveMotor.getEncoder();
		m_driveEncoder.setPositionConversionFactor(kWheelCircumference / kDriveGearRatio);
        m_driveEncoder.setVelocityConversionFactor(kWheelCircumference / kDriveGearRatio / 60);
		drivePosition = m_driveEncoder::getPosition;
		driveVelocity = m_driveEncoder::getVelocity;
		
		m_drivePIDController = m_driveMotor.getPIDController();

		// Setup Turning Motor
		m_turningMotor = new CANSparkMax(turningMotorChannel, MotorType.kBrushless);
		m_turningMotor.setIdleMode(IdleMode.kBrake);
		m_turningMotor.setInverted(true);

		m_turningEncoder = new CANCoder(turningEncoderChannel);
		m_turningEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 20);
		m_turningEncoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 100);
		//m_turningEncoder.configMagnetOffset(turnOffset);
		m_turningEncoder.configFeedbackCoefficient(
			2 * Math.PI / kEncoderResolution,
			"radians",
			SensorTimeBase.PerSecond);
		m_turningEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);
		m_turningMotor.setClosedLoopRampRate(0);
		// m_turningPIDController.setIntegratorRange(-3, 3);

		turningAngle = m_turningEncoder::getPosition;

		this.location = location;

		m_turningPIDController.enableContinuousInput(0, 2*Math.PI);
		m_turningPIDController.setTolerance(0.01745);

		SendableRegistry.addLW(this, "Swerve " + driveMotorChannel);
		SendableRegistry.addChild(this, m_driveMotor);
		SendableRegistry.addChild(this, m_drivePIDController);
		SendableRegistry.addChild(this, m_turningMotor);
		SendableRegistry.addChild(this, m_turningPIDController);
		SendableRegistry.addChild(this, m_turningEncoder);
	}

	/**
	 * Returns the current state of the module.
	 *
	 * @return The current state of the module.
	 */
	public SwerveModuleState getState() {
		return new SwerveModuleState(m_driveEncoder.getVelocity(),
				new Rotation2d(turningAngle.getAsDouble()));
	}

	/**
	 * Returns the current position of the module.
	 *
	 * @return The current position of the module.
	 */
	public SwerveModulePosition getPosition() {
		return new SwerveModulePosition(
				drivePosition.getAsDouble(), new Rotation2d(turningAngle.getAsDouble()));
	}

	public double getTurnKs() {
		return m_turnFeedforward.ks;
	}

	public double getTurnKv() {
		return m_turnFeedforward.kv;
	}

	public double getDriveFF() {
		return m_driveFeedforward;
	}

	public void setDriveFF(double ff) {
		m_driveFeedforward = ff;
	}

	/**
	 * Sets the desired state for the module.
	 *
	 * @param desiredState Desired state with speed and angle.
	 */
	public void setDesiredState(SwerveModuleState desiredState) {
		// Optimize the reference state to avoid spinning further than 90 degrees
		SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(turningAngle.getAsDouble()));
		// SwerveModuleState state = desiredState;

		// Calculate the turning motor output from the turning PID controller.
		final double turnOutput = m_turningPIDController.calculate(turningAngle.getAsDouble(),
				state.angle.getRadians());
		// final double turnFeedforward = m_turnFeedforward.calculate(m_turningPIDController.getSetpoint().velocity);
		// m_turningMotor.setVoltage(turnOutput + turnFeedforward);
		m_turningMotor.setVoltage(turnOutput);

		// Calculate drive motor output using SparkMax built-in PID controller.
		// final double speedRpm = state.speedMetersPerSecond * 60 / (kWheelCircumference);
		// m_drivePIDController.setReference(speedRpm, ControlType.kSmartVelocity, kDrivePidChannel);
		m_drivePIDController.setReference(state.speedMetersPerSecond, ControlType.kSmartVelocity, kDrivePidChannel);
		m_drivePIDController.setFF(m_driveFeedforward);
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.addIntegerProperty("Drive ID", m_driveMotor::getDeviceId, null);
		builder.addIntegerProperty("Turn ID", m_turningMotor::getDeviceId, null);
		builder.addDoubleProperty("Position", drivePosition, null);
		builder.addDoubleProperty("Velocity", driveVelocity, null);
		builder.addDoubleProperty("Drive Output", m_driveMotor::get, null);
		builder.addDoubleProperty("Heading", turningAngle, null);
		// builder.addDoubleProperty("Target Angle", () -> m_turningPIDController.getSetpoint().position, null);
	}

}
