package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class StopShoot extends InstantCommand {
	Shooter2016 shooter;

	public StopShoot(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		shooter.stopShooter();
	}

}
