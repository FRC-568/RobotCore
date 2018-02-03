package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class TiltUpwards extends Command {
	Shooter2016 shooter;

	public TiltUpwards() {

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
		shooter.tiltUp();
		System.out.println("Tilt Up");
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (Robot.getInstance().oi.shootThree.get())
			return false;
		else
			return true;

	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		shooter.stopTilt();
	}

	@Override
	protected void interrupted() {
		end();
		// TODO Auto-generated method stub
	}

}