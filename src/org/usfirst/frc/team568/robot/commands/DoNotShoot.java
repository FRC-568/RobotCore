package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class DoNotShoot extends Command {
	Shooter2016 shooter;

	public DoNotShoot() {
		shooter = Robot.getInstance().shooter;
		// requires(shooter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		shooter.stopShooter();
		System.out.println("Do not shoot");
	}

	@Override
	protected void execute() {
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
