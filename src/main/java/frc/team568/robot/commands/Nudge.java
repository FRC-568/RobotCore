package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class Nudge extends Command {
	Shooter2016 shooter;

	public Nudge() {
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void execute() {
		shooter.nudge();
		System.out.println("Nudge");
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		shooter.stopnudge();
	}

	@Override
	protected void interrupted() {
		Robot.getInstance().shooter.stopnudge();
	}

}
