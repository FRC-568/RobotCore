package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.StrongholdBot;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class TiltDownwards extends Command {
	Shooter shooter;

	public TiltDownwards() {

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
		shooter.tiltDown();
		System.out.println("Tilt down");
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (StrongholdBot.getInstance().oi.shootTwo.get())
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
		// TODO Auto-generated method stub
		end();
	}

}
