package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.WinchClimber;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbWithWinch extends Command {
	public WinchClimber winchClimber;

	public ClimbWithWinch() {

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		winchClimber.climber.set(1);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		winchClimber.climber.set(0);

	}

	@Override
	protected void interrupted() {
		winchClimber.climber.set(0);

	}

}
