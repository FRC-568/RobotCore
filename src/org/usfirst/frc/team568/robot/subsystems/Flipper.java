package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.commands.LiftFlipper;
import org.usfirst.frc.team568.robot.commands.LowerFlipper;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Flipper extends Subsystem {
	Solenoid lift1;
	Solenoid lift2;
	Solenoid lift3;
	private final Robot robot;

	@Override
	protected void initDefaultCommand() {
	}

	public Flipper() {
		robot = Robot.getInstance();

		robot.oi.liftFlipper.whenPressed(new LiftFlipper());
		robot.oi.lowerFlipper.whenPressed(new LowerFlipper());

		lift1 = new Solenoid(5);
		lift2 = new Solenoid(6);
		lift3 = new Solenoid(4);
	}

	public void liftUp() {
		this.lift1.set(true);
		this.lift2.set(true);
		this.lift3.set(true);
	}

	public void liftDown() {
		this.lift1.set(false);
		this.lift2.set(false);
		this.lift3.set(false);
	}
}
