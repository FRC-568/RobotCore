package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class TiltDownwards extends Command {
	Shooter2016 shooter;

	public TiltDownwards() {
		// requires(shooter);
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void execute() {
		shooter.tiltDown();
		System.out.println("Tilt down");
	}

	@Override
	protected boolean isFinished() {
		if (Robot.getInstance().oi.shootTwo.get())
			return false;
		else
			return true;
	}

	@Override
	protected void end() {
		shooter.stopTilt();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
