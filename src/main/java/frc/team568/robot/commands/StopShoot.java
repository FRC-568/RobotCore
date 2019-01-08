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

		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		System.out.println("Stop Shoot");
		shooter.stopShooter();
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
