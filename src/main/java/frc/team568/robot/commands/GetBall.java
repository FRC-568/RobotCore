package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class GetBall extends Command {
	Shooter2016 shooter;

	public GetBall() {
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void execute() {
		shooter.obtainBall();
		System.out.println("Get Ball");
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
