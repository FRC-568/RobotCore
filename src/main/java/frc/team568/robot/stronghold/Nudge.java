package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Nudge extends InstantCommand {
	Shooter2016 shooter;

	public Nudge(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		shooter.nudge();
	}

	@Override
	public void end(boolean interrupted) {
		shooter.stopnudge();
	}

}
