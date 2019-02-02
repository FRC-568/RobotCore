package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.ArcadeDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive2016 extends Command {
	ArcadeDrive drive;
	Timer timer;
	double speed;
	double delay;
	boolean forward;

	@Override
	protected void initialize() {
		drive = Robot.getInstance().arcadeDrive;
		timer = new Timer();
		speed = SmartDashboard.getNumber("Speed", 0.0);
		delay = SmartDashboard.getNumber("Time?", 0.0);
		forward = SmartDashboard.getBoolean("Forward?", true);
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
	}

	@Override
	protected void interrupted() {
	}

}