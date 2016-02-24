package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class nudge extends Command {

	public nudge() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		Robot.getInstance().shooter.nudge();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().oi.shootOne.get()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().shooter.stopnudge();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		Robot.getInstance().shooter.stopnudge();
	}

}
