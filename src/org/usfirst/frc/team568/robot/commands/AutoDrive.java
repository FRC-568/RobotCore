package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;
import org.usfirst.frc.team568.robot.subsystems.Arms;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive extends Command {
	ArcadeDrive drive;
	Shooter shooter;
	Arms arms;

	public AutoDrive() {
		drive = Robot.getInstance().arcadeDrive;
		shooter = Robot.getInstance().shooter;
		arms = Robot.getInstance().arms;
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		double time = SmartDashboard.getNumber("Time?");

		if (SmartDashboard.getBoolean("Forward?") == true) {
			shooter.tiltUp();
			Timer.delay(.75);
			shooter.stopTilt();
			drive.forwardWithGyro(SmartDashboard.getNumber("Speed"));
		} else
			drive.goBackwards(SmartDashboard.getNumber("Speed"));
		Timer.delay(time);
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
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
