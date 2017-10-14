package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.StrongholdBot;
import org.usfirst.frc.team568.robot.subsystems.Arms;

import edu.wpi.first.wpilibj.command.Command;

public class ArmDown extends Command {
	Arms arms;

	public ArmDown() {

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {

		arms = StrongholdBot.getInstance().arms;
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {

		StrongholdBot.getInstance().arms.goDown();
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (StrongholdBot.getInstance().oi.armsDown.get())
			return false;
		else
			return true;

	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		StrongholdBot.getInstance().arms.stop();
	}

	@Override
	protected void interrupted() {
		StrongholdBot.getInstance().arms.stop();
		// TODO Auto-generated method stub

	}

}
