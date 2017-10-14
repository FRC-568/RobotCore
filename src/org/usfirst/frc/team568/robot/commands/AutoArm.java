package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.StrongholdBot;
import org.usfirst.frc.team568.robot.subsystems.Arms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoArm extends Command {
	Arms arm;
	Timer timer;

	@Override
	protected void initialize() {
		arm = StrongholdBot.getInstance().arms;
		timer = new Timer();
		timer.reset();
		timer.start();
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		arm.goDown();

		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (timer.get() < 2)
			return false;
		else
			return true;
		// TODO Auto-generated method stub

	}

	@Override
	protected void end() {
		arm.stop();
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
