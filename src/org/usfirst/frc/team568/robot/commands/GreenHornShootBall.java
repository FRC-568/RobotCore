package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.GreenHorn;

import edu.wpi.first.wpilibj.command.Command;

public class GreenHornShootBall extends Command {
	GreenHorn shooter;

	public GreenHornShootBall() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		shooter.shootBall(1);

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return !Robot.getInstance().oi.one.get();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		shooter.setSpeed(0);
		shooter.nudgerNeutral();
	}

	@Override
	protected void interrupted() {
		end();

	}

}
