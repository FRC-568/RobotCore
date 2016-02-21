package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.CrateBot;
import org.usfirst.frc.team568.robot.subsystems.CrateLifter;

import edu.wpi.first.wpilibj.command.Command;

public class Lifter extends Command {
	CrateLifter crateLifter = CrateBot.getInstance().crateLifter;

	@Override
	protected void initialize() {
		CrateBot.getInstance().crateLifter.lift();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		CrateBot.getInstance().crateLifter.brake();
	}

	@Override
	protected void interrupted() {
		CrateBot.getInstance().crateLifter.brake();
	}
}
