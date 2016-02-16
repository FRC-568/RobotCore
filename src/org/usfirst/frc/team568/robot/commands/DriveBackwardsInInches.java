package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame;

import edu.wpi.first.wpilibj.command.Command;

public class DriveBackwardsInInches extends Command {
	double speed;
	double inches;
	ReferenceFrame referenceframe;

	public DriveBackwardsInInches(double speed, double inches) {
		this.speed = speed;
		this.inches = inches;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		Robot.getInstance().referenceframe.stayTrueToHeading(0);
		Robot.getInstance().referenceframe.travelBackwardsToDistance(inches, speed);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().referenceframe.imu.getDisX() < 0) {
			return true;
			// return false;
		}
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().meccanumDrive.halt();

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

		end();

	}

}
