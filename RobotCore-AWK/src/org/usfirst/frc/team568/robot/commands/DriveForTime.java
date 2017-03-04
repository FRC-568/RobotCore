package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class DriveForTime extends TimedCommand {

	private final double speed;
	private final double time;
	private final DriveTrain driveTrain;
	private double timeStamp;

	public DriveForTime(double time, double speed, DriveTrain driveTrain) {
		super(time);
		this.time = time;
		this.speed = speed;
		this.driveTrain = driveTrain;
		timeStamp = Timer.getFPGATimestamp();

	}

	protected void execute() {
		driveTrain.setSpeed(speed, speed);
	}

	@Override
	protected boolean isFinished() {
		if (Timer.getFPGATimestamp() - timeStamp > time) {
			return true;
		}

		return false;
	}

	@Override
	protected void end() {
		driveTrain.halt();
	}

	@Override
	protected void interrupted() {

		driveTrain.halt();// end();
	}

}
