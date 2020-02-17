package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TiltDownwards extends CommandBase {
	Shooter2016 shooter;

	@Override
	public void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	public void execute() {
		shooter.tiltDown();
		System.out.println("Tilt down");
	}

	@Override
	public boolean isFinished() {
		return !Robot.getInstance().oi.shootTwo.get();
	}

	@Override
	public void end(boolean interrupted) {
		shooter.stopTilt();
	}

}
