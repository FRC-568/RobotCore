package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.Robot;
import org.usfirst.frc.team568.robot.subsystems.WinchClimber;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbWithWinch extends Command {
	public WinchClimber winchClimber;

	public ClimbWithWinch() {
		winchClimber = Robot.getInstance().climber;

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		winchClimber.lift_m.set(-1);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		winchClimber.lift_m.set(0);

	}

	@Override
	protected void interrupted() {
		end();

	}

}
