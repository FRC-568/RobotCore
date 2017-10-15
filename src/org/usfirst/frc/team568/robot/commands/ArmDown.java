package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.subsystems.Arms;

import edu.wpi.first.wpilibj.command.Command;

public class ArmDown extends Command {
	Arms arms;

	public ArmDown() {

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {

		arms = Robot.getInstance().arms;
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {

		Robot.getInstance().arms.goDown();
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (Robot.getInstance().oi.armsDown.get())
			return false;
		else
			return true;

	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().arms.stop();
	}

	@Override
	protected void interrupted() {
		Robot.getInstance().arms.stop();
		// TODO Auto-generated method stub

	}

}
