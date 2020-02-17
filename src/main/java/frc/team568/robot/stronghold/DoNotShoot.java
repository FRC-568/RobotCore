package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DoNotShoot extends CommandBase {
	Shooter2016 shooter;

	public DoNotShoot() {
		shooter = Robot.getInstance().shooter;
		// requires(shooter);
	}

	@Override
	public void initialize() {
		shooter.stopShooter();
		System.out.println("Do not shoot");
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
