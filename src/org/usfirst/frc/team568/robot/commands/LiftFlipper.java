package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.CrateBot;
import org.usfirst.frc.team568.robot.subsystems.Flipper;

import edu.wpi.first.wpilibj.command.Command;

public class LiftFlipper extends Command {
	Flipper flipper = CrateBot.getInstance().flipper;

	@Override
	protected void initialize() {
		CrateBot.getInstance().flipper.liftUp();
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
