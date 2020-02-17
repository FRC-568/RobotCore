package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class GetBall extends CommandBase {
	Shooter2016 shooter;

	@Override
	public void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	public void execute() {
		shooter.obtainBall();
		System.out.println("Get Ball");
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
