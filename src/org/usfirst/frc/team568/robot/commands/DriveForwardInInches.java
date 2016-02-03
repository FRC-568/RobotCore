package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame;

import edu.wpi.first.wpilibj.command.Command;

public class DriveForwardInInches extends Command {

	ReferenceFrame referenceframe;

	public DriveForwardInInches(double speed, double inches) {

	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		referenceframe.calabrateimu();
		referenceframe.resetData();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
