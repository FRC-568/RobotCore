package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TiltUpwards extends CommandBase {
	Shooter2016 shooter;

	@Override
	public void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	public void execute() {
		shooter.tiltUp();
		System.out.println("Tilt Up");
	}

	@Override
	public boolean isFinished() {
		return !Robot.getInstance().oi.shootThree.get();
	}

	@Override
	public void end(boolean interrupted) {
		shooter.stopTilt();
	}

}
