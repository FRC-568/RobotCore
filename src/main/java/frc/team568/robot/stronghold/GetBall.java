package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class GetBall extends InstantCommand {
	Shooter2016 shooter;

	public GetBall(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		shooter.obtainBall();
	}

}
