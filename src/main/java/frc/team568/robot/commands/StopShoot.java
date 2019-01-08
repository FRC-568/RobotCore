package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class StopShoot extends Command {
	Shooter2016 shooter;

	public StopShoot() {
		// requires(shooter);
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void execute() {
		System.out.println("Stop Shoot");
		shooter.stopShooter();
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
