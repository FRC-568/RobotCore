// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.chargedup;

import static frc.team568.robot.chargedup.Constants.SwerveConstants.kMaxSpinRate;
import static frc.team568.robot.chargedup.Constants.SwerveConstants.kModuleMaxAngularAcceleration;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;

/** Add your docs here. */
public class TurnToHeading extends Command {
	private static final int HOLD_MAX = 3;
	private int holdCount = 0;

	public final SwerveSubsystem drive;
	private final Rotation2d targetHeading;
	private final ProfiledPIDController m_turningPIDController = new ProfiledPIDController(
			0.05,
			0,
			0,
			new TrapezoidProfile.Constraints(
					kMaxSpinRate, kModuleMaxAngularAcceleration));
	private final SimpleMotorFeedforward m_turnFeedforward = new SimpleMotorFeedforward(0.08, 0.04);

	public TurnToHeading(final SwerveSubsystem drive, Rotation2d heading) {
		this.drive = drive;
		this.targetHeading = heading;
		addRequirements(drive);

		m_turningPIDController.enableContinuousInput(0, 2 * Math.PI);
		m_turningPIDController.setTolerance(Math.PI / 180);
	}

	@Override
	public void execute() {

		// Calculate the turning motor output from the turning PID controller.
		final double turnOutput = m_turningPIDController.calculate(drive.getHeading().getRadians(),
				targetHeading.getRadians());

		final double turnFeedforward = m_turnFeedforward.calculate(m_turningPIDController.getSetpoint().velocity);

		drive.drive(0, 0, turnOutput + turnFeedforward, true);
	}

	@Override
	public boolean isFinished() {
		if (m_turningPIDController.atGoal()) {
			if (holdCount < HOLD_MAX) {
				holdCount++;
			} else {
				return true;
			}
		} else {
			holdCount = 0;
		}
		return false;
	}

	@Override
	public void end(boolean isInterrupted) {
		holdCount = 0;
	}

}
