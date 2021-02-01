package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TiltUpwards extends CommandBase {
	Shooter2016 shooter;

	public TiltUpwards(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		shooter.tiltUp();
	}

	@Override
	public void end(boolean interrupted) {
		shooter.stopTilt();
	}

}
