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

	}

	@Override
	protected void execute() {
		shooter.shooter.set(-1);
		Timer.delay(.5);
		shooter.gate.set(1);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.shooter.set(0);
		shooter.gate.set(0);

	}

	@Override
	protected void interrupted() {
		shooter.shooter.set(0);
		shooter.gate.set(0);

	}

}
