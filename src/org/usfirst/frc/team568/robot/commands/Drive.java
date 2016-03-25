package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Command {
	ArcadeDrive drive;
	Timer timer;
	double speed;
	double delay;
	boolean forward;

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		drive = Robot.getInstance().arcadeDrive;
		timer = new Timer();
		speed = SmartDashboard.getNumber("Speed");
		delay = SmartDashboard.getNumber("Time?");
		forward = SmartDashboard.getBoolean("Forward?");
		timer.reset();
		timer.start();

	}

	@Override
	protected void execute() {

		if (forward) {

			drive.forwardWithGyro(speed);
		} else if (!forward) {
			drive.reverseWithGyro(speed);
		}

		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (timer.get() < delay)
			return false;
		else
			return true;
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
