package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turn extends Command {
	DriveTrain drive;
	double degrees;
	double ra;
	ReferenceFrame2 ref;

	public Turn(double degrees) {
		this.degrees = degrees;

	}

	public Turn() {
		// degrees = SmartDashboard.getNumber("Degrees", 90);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		drive = Robot.getInstance().driveTrain;
		ref = Robot.getInstance().referanceFrame2;
		// ra = Robot.getInstance().referanceFrame2.getAngle() + degrees;

	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("GYRO", ref.getAngle());
		if (degrees > 0) {
			drive.turnRight(.3);
		} else if (degrees < 0) {
			drive.turnLeft(.3);

		} else {
			drive.halt();
		}

		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (degrees < 0) {
			if (ref.getAngle() < degrees) {
				return (true);
			} else {
				return (false);
			}
		} else if (degrees > 0) {
			if (ref.getAngle() > degrees)
				return true;
			else
				return false;
		} else
			return true;

	}

	@Override
	protected void end() {
		drive.halt();
		Timer.delay(1);
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
