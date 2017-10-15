package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.steamworks.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter2017;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Shoot2017 extends Command {
	public Shooter2017 shooter;
	private boolean gateState;
	private double timeStamp;
	private boolean rampedUp;

	public Shoot2017() {
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

		shooter.shootMotor.set(-(1));
		if (!rampedUp) {
			if ((Timer.getFPGATimestamp() - timeStamp) >= 1.75)
				rampedUp = true;
		} else if ((Timer.getFPGATimestamp() - timeStamp) >= .75) {
			if (gateState) {
				shooter.gate.setAngle(0);
				gateState = false;
			} else {
				shooter.gate.setAngle(100);
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
		rampedUp = false;
	}

	@Override
	protected void interrupted() {
		end();
	}
}
