package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.CrateBot;
import org.usfirst.frc.team568.robot.subsystems.CrateLifter;

import edu.wpi.first.wpilibj.command.Command;

public class Brake extends Command {
	CrateLifter crateLifter = CrateBot.getInstance().crateLifter;

	@Override
	protected void initialize() {
		CrateBot.getInstance().crateLifter.brake();
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
