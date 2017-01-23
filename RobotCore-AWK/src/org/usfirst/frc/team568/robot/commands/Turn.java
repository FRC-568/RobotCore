package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turn extends Command {
	DriveTrain drive;
	double degrees;
	double ra;

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		drive = Robot.getInstance().arcadeDrive;
		degrees = SmartDashboard.getNumber("Degrees", 90);
		ra = Robot.getInstance().referanceFrame2.getAngle() + degrees;

	}

	@Override
	protected void execute() {
		drive.turnRight(.5);

		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if ((Robot.getInstance().referanceFrame2.getAngle()) > ra
				&& (Robot.getInstance().referanceFrame2.getAngle() - 2) < ra)
			return true;
		else
			return false;
	}

	@Override
	protected void end() {
		drive.halt();
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
