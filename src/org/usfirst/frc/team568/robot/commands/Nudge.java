package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class Nudge extends Command {
	Shooter2016 shooter;

	public Nudge() {

		// requires(shooter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;

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
		Robot.getInstance().shooter.stopnudge();
	}

}
