package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Nudge extends CommandBase {
	Shooter2016 shooter;

	@Override
	public void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	public void execute() {
		shooter.nudge();
		System.out.println("Nudge");
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end(boolean interrupted) {
		shooter.stopnudge();
	}

}
