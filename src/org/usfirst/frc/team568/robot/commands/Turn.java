package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.steamworks.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2017;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turn extends Command {
	DriveTrain drive;
	double degrees;
	double ra;
	ReferenceFrame2017 ref;

	public Turn(double degrees) {
		this.degrees = degrees;
	}

	public Turn() {
	}

	@Override
	protected void initialize() {
		drive = Robot.getInstance().driveTrain;
		ref = Robot.getInstance().referenceFrame;
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("GYRO", ref.getAngle());
		if (degrees > 0)
			drive.turnRight(.3);
		else if (degrees < 0)
			drive.turnLeft(.3);
		else
			drive.halt();
	}

	@Override
	protected boolean isFinished() {
		if (degrees < 0) {
			return ref.getAngle() < degrees;
		} else if (degrees > 0) {
			return ref.getAngle() > degrees;
		} else
			return true;

	}

	@Override
	protected void end() {
		drive.halt();
		Timer.delay(1);
	}

}
