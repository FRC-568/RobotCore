package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TiltDownwards extends CommandBase {
	Shooter2016 shooter;

	public TiltDownwards(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		shooter.tiltDown();
	}

	@Override
	public void end(boolean interrupted) {
		shooter.stopTilt();
	}

}
