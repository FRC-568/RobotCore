package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Shoot2016 extends CommandBase {
	Shooter2016 shooter;

	@Override
	public void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	public void execute() {
		System.out.println("Shoot");
		shooter.shoot();
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
