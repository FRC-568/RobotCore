package frc.team568.robot.commands;

import frc.team568.robot.stronghold.Robot;
import frc.team568.robot.subsystems.Shooter2016;

import edu.wpi.first.wpilibj.command.Command;

public class TiltUpwards extends Command {
	Shooter2016 shooter;

	public TiltUpwards() {
		// requires(shooter);
	}

	@Override
	protected void initialize() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void execute() {
		shooter.tiltUp();
		System.out.println("Tilt Up");
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
		shooter.stopTilt();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
