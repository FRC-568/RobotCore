package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Stop extends Command {

	public Stop() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		Robot.getInstance().tankDrive.halt();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().tankDrive.halt();

	}

	@Override
	protected void interrupted() {
		Robot.getInstance().tankDrive.halt();
		// TODO Auto-generated method stub

	}

}
