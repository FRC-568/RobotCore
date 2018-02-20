package org.usfirst.frc.team568.robot.powerup;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Turn2018 extends Command {
	DriveTrain2018 dt;
	double degrees;
	double ra;
	// ReferenceFrame2017 ref;

	public Turn2018(DriveTrain2018 dt, double degrees) {
		this.dt = dt;
		requires(dt);
		this.degrees = degrees;
	}

	public Turn2018() {
	}

	@Override
	protected void initialize() {
		System.out.println("TURNING " + degrees);
		// ref = Robot.getInstance().referenceFrame;
	}

	@Override
	protected void execute() {
		// SmartDashboard.putNumber("GYRO", ref.getAngle());
		if (degrees > 0)
			dt.turnRight(.3);
		else if (degrees < 0)
			dt.turnLeft(.3);
		else
			dt.stop();
	}

	@Override
	protected boolean isFinished() {
		if (degrees < 0) {
			return dt.getAngle() < degrees;
		} else if (degrees > 0) {
			return dt.getAngle() > degrees;
		} else
			return true;

	}

	@Override
	protected void end() {
		dt.stop();
		Timer.delay(1);
	}

}
