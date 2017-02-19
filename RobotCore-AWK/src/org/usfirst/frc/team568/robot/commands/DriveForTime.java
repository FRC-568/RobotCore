package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class DriveForTime extends TimedCommand {

	private final double speed;
	private final DriveTrain driveTrain;

	public DriveForTime(double time, double speed, DriveTrain driveTrain) {
		super(time);
		this.speed = speed;
		this.driveTrain = driveTrain;

	}

	protected void initialize() {
		driveTrain.leftBack.set(speed);
		driveTrain.leftFront.set(speed);
		driveTrain.rightBack.set(speed);
		driveTrain.rightFront.set(speed);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		driveTrain.leftBack.set(0);
		driveTrain.leftFront.set(0);
		driveTrain.rightBack.set(0);
		driveTrain.rightFront.set(0);
	}

	@Override
	protected void interrupted() {
		end();
	}

}
