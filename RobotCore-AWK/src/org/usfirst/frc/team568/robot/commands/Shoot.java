package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Shoot extends Command {
	public Shooter shooter;

	public Shoot() {
	}

	@Override
	protected void initialize() {
		shooter.shootMotor.set(-(7 / 12));
	}

	@Override
	protected void execute() {

		shooter.gate.setAngle(170);
		Timer.delay(.5);
		shooter.gate.setAngle(180);
		Timer.delay(.5);

	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		shooter.shootMotor.set(0);

	}

	@Override
	protected void interrupted() {
		shooter.shootMotor.set(0);

	}

}
