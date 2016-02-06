package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.CrateLifter;

import edu.wpi.first.wpilibj.command.Command;

public class Reverse extends Command {
	CrateLifter crateLifter = Robot.getInstance().crateLifter;

	@Override
	protected void initialize() {
		Robot.getInstance().crateLifter.reverse();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
}
