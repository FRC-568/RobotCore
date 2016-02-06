package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TurnToHeading extends Command {
	double speed;
	double position;

	TurnToHeading(double speed, double position) {
		this.position = position;
		this.speed = speed;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		Robot.getInstance().referenceframe.TurnToHeading(speed, position);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().referenceframe.TurnToHeading(speed, position) == true) {
			return true;
		}
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().drive.halt();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		end();

	}
}
