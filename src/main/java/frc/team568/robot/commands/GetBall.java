package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class GetBall extends Command {
	Shooter2016 shooter;

	public GetBall() {

		// requires(shooter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		shooter.obtainBall();
		System.out.println("Get Ball");
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
