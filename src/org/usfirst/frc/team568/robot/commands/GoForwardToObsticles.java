package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class GoForwardToObsticles extends Command {
	Timer timer;
	double time;

	public GoForwardToObsticles(double time) {
		this.time = time;
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		timer.start();
		Robot.getInstance().tankDrive.forwardWithGyro(.7);
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (timer.hasPeriodPassed(time) == true) {
			return true;

		}
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		timer.stop();
		Robot.getInstance().tankDrive.halt();
	}

	@Override
	protected void interrupted() {
		timer.stop();
		Robot.getInstance().tankDrive.halt();
		// TODO Auto-generated method stub

	}

}
