package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Command {
	DriveTrain drive;
	Timer timer;

	double speed;
	double delay;
	boolean forward;
	double CM;
	ReferenceFrame2 ref;
	double sign;

	public Drive(double distance, double speed) {
		ref = Robot.getInstance().referanceFrame2;
		CM = ref.motorEncoder.getDistance() + (distance);
		this.speed = speed;
		sign = distance;

	}

	public Drive() {
		ref = Robot.getInstance().referanceFrame2;
		SmartDashboard.getNumber("Speed", 0);
		CM = 0;
	}

	@Override
	protected void initialize() {
		drive = Robot.getInstance().driveTrain;

		timer = new Timer();
		timer.reset();
		timer.start();
		ref.motorEncoder.reset();
		ref.reset();

	}

	@Override
	protected void execute() {

		drive.forwardWithGyro(speed);
		SmartDashboard.putNumber("GYRO", ref.getAngle());

	}

	@Override
	protected boolean isFinished() {
		if (sign > 0) {
			if (ref.motorEncoder.getDistance() > CM)
				return true;
			else
				return false;
		} else {
			if (ref.motorEncoder.getDistance() < CM)
				return true;
			else
				return false;
		}
	}

	@Override
	protected void end() {
		drive.halt();
		Timer.delay(2);
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
