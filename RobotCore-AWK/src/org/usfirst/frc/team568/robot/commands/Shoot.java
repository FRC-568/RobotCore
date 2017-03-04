package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Shoot extends Command {
	public Shooter shooter;
	private boolean gateState;
	private double timeStamp;
	private boolean rampedUp;

	public Shoot() {
		shooter = Robot.getInstance().shooter;

	}

	@Override
	protected void initialize() {
		shooter.shootMotor.set(-(1.0));
		gateState = false;
		timeStamp = Timer.getFPGATimestamp();

	}

	@Override
	protected void execute() {

		shooter.shootMotor.set(-(8.5 / 12.0));
		if (!rampedUp) {
			if ((Timer.getFPGATimestamp() - timeStamp) >= 2.5)
				rampedUp = true;
		} else if ((Timer.getFPGATimestamp() - timeStamp) >= .5) {
			if (gateState) {
				shooter.gate.setAngle(0);
				gateState = false;
			} else {
				shooter.gate.setAngle(50);
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
		shooter.gate.setAngle(0);
		gateState = false;
	}

	@Override
	protected void interrupted() {
		end();
	}
}
