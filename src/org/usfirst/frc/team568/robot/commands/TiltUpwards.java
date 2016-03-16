package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TiltUpwards extends Command {

	public TiltUpwards() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

		Robot.getInstance().shooter.tiltUp();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().oi.shootThree.get() || Robot.getInstance().shooter.upperLimmitSwitch.get()) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().shooter.stopTilt();
	}

	@Override
	protected void interrupted() {
		Robot.getInstance().shooter.stopTilt();
		// TODO Auto-generated method stub
	}

}
