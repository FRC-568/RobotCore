package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame;

import edu.wpi.first.wpilibj.command.Command;

public class DriveForwardInInches extends Command {
	double speed;
	double inches;
	ReferenceFrame referenceframe;

	public DriveForwardInInches(double speed, double inches) {
		this.speed = speed;
		this.inches = inches;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		// referenceframe.calabrateimu();
		// referenceframe.resetData();

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		// Robot.getInstance().referenceframe.stayTrueToHeading(0);
		Robot.getInstance().referenceframe.travelForwardToDistance(speed);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().referenceframe.imu.getDisX() > inches) {
			return true;
			// return false;
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
		end();
		// TODO Auto-generated method stub

	}

}
