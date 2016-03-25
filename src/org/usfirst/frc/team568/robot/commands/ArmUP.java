package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.Arms;

import edu.wpi.first.wpilibj.command.Command;

public class ArmUP extends Command {
	Arms arms;

	public ArmUP() {

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {

		arms = Robot.getInstance().arms;

	}

	@Override
	protected void execute() {
		Robot.getInstance().arms.goUp();
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (Robot.getInstance().oi.armsUp.get())
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
		// TODO Auto-generated method stub
		Robot.getInstance().arms.stop();
	}

}
