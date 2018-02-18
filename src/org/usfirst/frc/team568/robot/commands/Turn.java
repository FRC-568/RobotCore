package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.DriveTrain2018;
import org.usfirst.frc.team568.robot.powerup.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Turn extends Command {
	DriveTrain2018 drive;
	double degrees;
	double ra;
	// ReferenceFrame2017 ref;

	public Turn(double degrees) {
		this.degrees = degrees;
	}

	public Turn() {
	}

	@Override
	protected void initialize() {
		drive = Robot.getInstance().driveTrain;
		// ref = Robot.getInstance().referenceFrame;
	}

	@Override
	protected void execute() {
		// SmartDashboard.putNumber("GYRO", ref.getAngle());
		if (degrees > 0)
			drive.turnRight(.3);
		else if (degrees < 0)
			drive.turnLeft(.3);
		else
			drive.stop();
	}

	@Override
	protected boolean isFinished() {
		if (degrees < 0) {
			return drive.getAngle() < degrees;
		} else if (degrees > 0) {
			return drive.getAngle() > degrees;
		} else
			return true;

	}

	@Override
	protected void end() {
		drive.stop();
		Timer.delay(1);
	}

}
