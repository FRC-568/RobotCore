package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShooter extends Command {
	Shooter2016 shooter;

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		if (SmartDashboard.getBoolean("Forward?", true)) {
			shooter.tiltUp();
			Timer.delay(.75);
			shooter.stopTilt();
		}
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
