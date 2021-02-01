package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Shoot2016 extends InstantCommand {
	Shooter2016 shooter;

	public Shoot2016(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		shooter.shoot();
	}

}
