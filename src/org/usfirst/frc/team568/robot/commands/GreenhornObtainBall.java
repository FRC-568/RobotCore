package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.GreenHorn;

import edu.wpi.first.wpilibj.command.Command;

public class GreenhornObtainBall extends Command {
	GreenHorn shooter;

	public GreenhornObtainBall() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		shooter.obtainBall(.7);
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return !Robot.getInstance().oi.two.get();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		shooter.setSpeed(0);
		shooter.nudgerNeutral();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		end();

	}

}
