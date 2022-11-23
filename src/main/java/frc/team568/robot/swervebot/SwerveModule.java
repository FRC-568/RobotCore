// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.swervebot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class SwerveModule {
	private static final double kWheelRadius = 0.047625; //3.75 inch wheels on mk4i
	private static final int kEncoderResolution = 4096;

	private static final double kModuleMaxAngularVelocity = Drivetrain.kMaxAngularSpeed;
	private static final double kModuleMaxAngularAcceleration =
			2 * Math.PI; // radians per second squared

	private final CANSparkMax m_driveMotor;
	private final CANSparkMax m_turningMotor;

	private final RelativeEncoder m_driveEncoder;
	private final RelativeEncoder m_turningEncoder;

	// Gains are for example purposes only - must be determined for your own robot!
	private final PIDController m_drivePIDController = new PIDController(1, 0, 0);

	// Gains are for example purposes only - must be determined for your own robot!
	private final ProfiledPIDController m_turningPIDController =
			new ProfiledPIDController(
					1,
					0,
					0,
					new TrapezoidProfile.Constraints(
							kModuleMaxAngularVelocity, kModuleMaxAngularAcceleration));

	// Gains are for example purposes only - must be determined for your own robot!
	private final SimpleMotorFeedforward m_driveFeedforward = new SimpleMotorFeedforward(1, 3);
	private final SimpleMotorFeedforward m_turnFeedforward = new SimpleMotorFeedforward(1, 0.5);

	/**
	 * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
	 *
	 * @param driveMotorChannel CAN output for the drive motor.
	 * @param turningMotorChannel CAN output for the turning motor.
	 */
	public SwerveModule(
			int driveMotorChannel,
			int turningMotorChannel) {
		m_driveMotor = new CANSparkMax(driveMotorChannel, MotorType.kBrushless);
		m_turningMotor = new CANSparkMax(turningMotorChannel, MotorType.kBrushless);

		m_driveEncoder = m_driveMotor.getEncoder();
		m_driveEncoder.setVelocityConversionFactor(2 * Math.PI * kWheelRadius / 60);

		m_turningEncoder = m_turningMotor.getAlternateEncoder(kEncoderResolution);
		m_turningEncoder.setPositionConversionFactor(2 * Math.PI);

		// Limit the PID Controller's input range between -pi and pi and set the input
		// to be continuous.
		m_turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
	}

	/**
	 * Returns the current state of the module.
	 *
	 * @return The current state of the module.
	 */
	public SwerveModuleState getState() {
		return new SwerveModuleState(m_driveEncoder.getVelocity(), new Rotation2d(m_turningEncoder.getPosition()));
	}

	/**
	 * Sets the desired state for the module.
	 *
	 * @param desiredState Desired state with speed and angle.
	 */
	public void setDesiredState(SwerveModuleState desiredState) {
		// Optimize the reference state to avoid spinning further than 90 degrees
		SwerveModuleState state =
				SwerveModuleState.optimize(desiredState, new Rotation2d(m_turningEncoder.getPosition()));

		// Calculate the drive output from the drive PID controller.
		final double driveOutput =
				m_drivePIDController.calculate(m_driveEncoder.getVelocity(), state.speedMetersPerSecond);

		final double driveFeedforward = m_driveFeedforward.calculate(state.speedMetersPerSecond);

		// Calculate the turning motor output from the turning PID controller.
		final double turnOutput =
				m_turningPIDController.calculate(m_turningEncoder.getPosition(), state.angle.getRadians());

		final double turnFeedforward =
				m_turnFeedforward.calculate(m_turningPIDController.getSetpoint().velocity);

		m_driveMotor.setVoltage(driveOutput + driveFeedforward);
		m_turningMotor.setVoltage(turnOutput + turnFeedforward);
	}
}
