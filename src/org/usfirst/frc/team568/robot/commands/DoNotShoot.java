package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.StrongholdBot;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class DoNotShoot extends Command {
	Shooter shooter;

	public DoNotShoot() {
		shooter = StrongholdBot.getInstance().shooter;
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
