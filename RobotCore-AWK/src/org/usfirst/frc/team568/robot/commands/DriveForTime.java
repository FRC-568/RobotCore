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

	protected void execute() {
		driveTrain.setSpeed(speed, speed);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		driveTrain.halt();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
