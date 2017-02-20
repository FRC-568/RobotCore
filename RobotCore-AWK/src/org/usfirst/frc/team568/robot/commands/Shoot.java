package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Shoot extends Command {
	public Shooter shooter;
	private boolean gateState;
	private double timeStamp;

	public Shoot() {
		shooter = Robot.getInstance().shooter;
		System.out.println("Shoot() has been called");
	}

	@Override
	protected void initialize() {
		System.out.println("initialized() has been called");
		shooter.shootMotor.set(-(7 / 12));

		timeStamp = Timer.getFPGATimestamp();

	}

	@Override
	protected void execute() {
		System.out.println("execute() has been called");

		shooter.shootMotor.set(-(7 / 12));
		if ((Timer.getFPGATimestamp() - timeStamp) >= .5) {
			if (gateState) {
				shooter.gate.setAngle(0);
				gateState = false;
			} else {
				shooter.gate.setAngle(20);
				gateState = true;
			}
			timeStamp = Timer.getFPGATimestamp();

		}

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.shootMotor.set(0);
		System.out.println("end() has been called");

	}

	@Override
	protected void interrupted() {
		shooter.shootMotor.set(0);
		System.out.println("interupted() has been called");

	}

}
