package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class DoNotShoot extends Command {
	Shooter2016 shooter;

	public DoNotShoot() {
		shooter = Robot.getInstance().shooter;
		// requires(shooter);
	}

	@Override
	protected void initialize() {
		shooter.stopShooter();
		System.out.println("Do not shoot");
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
