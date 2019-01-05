package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.Shooter2017;

import edu.wpi.first.wpilibj.command.Command;

public class StopTheShooter extends Command {
	public Shooter2017 shooter;

	public StopTheShooter() {
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		shooter.shootMotor.set(0);

	}

	@Override
	protected boolean isFinished() {
		return false;
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
