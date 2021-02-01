package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DoNotShoot extends InstantCommand {
	Shooter2016 shooter;

	public DoNotShoot(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void initialize() {
		shooter.stopShooter();
	}

}
