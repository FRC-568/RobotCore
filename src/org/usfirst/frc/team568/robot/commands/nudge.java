package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.StrongholdBot;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class nudge extends Command {
	Shooter shooter;

	public nudge() {

		// requires(shooter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		shooter = StrongholdBot.getInstance().shooter;

		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		shooter.nudge();
		System.out.println("Nudge");
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {

		return true;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		shooter.stopnudge();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		StrongholdBot.getInstance().shooter.stopnudge();
	}

}
