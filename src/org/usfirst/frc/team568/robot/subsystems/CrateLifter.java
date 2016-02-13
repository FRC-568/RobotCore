package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.commands.Brake;
import org.usfirst.frc.team568.robot.commands.CrateLifterIn;
import org.usfirst.frc.team568.robot.commands.CrateLifterOut;
import org.usfirst.frc.team568.robot.commands.Lifter;
import org.usfirst.frc.team568.robot.commands.Reverse;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CrateLifter extends Subsystem {
	private final Robot robot;
	protected Joystick driveStick;
	Solenoid in;
	Solenoid out;
	Solenoid brake;
	Solenoid brake1;
	SpeedController lift;
	public int pov;

	public CrateLifter() {
		this.robot = Robot.getInstance();
		this.lift = new Victor(5);

		this.driveStick = this.robot.oi.leftStick;

		this.in = new Solenoid(7);
		this.out = new Solenoid(3);
		this.brake = new Solenoid(2);
		this.brake1 = new Solenoid(1);

		this.robot.oi.crateIn.whenPressed(new CrateLifterIn());
		this.robot.oi.crateOut.whenPressed(new CrateLifterOut());
		this.robot.oi.liftGo.whenPressed(new Lifter());
		this.robot.oi.liftStop.whenPressed(new Brake());
		this.robot.oi.lifterReverse.whenPressed(new Reverse());
	}

	public void reverse() {
		this.brake.set(true);
		this.brake1.set(false);
		this.lift.set(-0.75D);
	}

	public void lift() {
		this.brake.set(true);
		this.brake1.set(false);
		this.lift.set(0.75D);
	}

	public void brake() {
		this.brake.set(false);
		this.brake1.set(true);
		this.lift.set(0.0D);
	}

	public void crateIn() {
		this.in.set(true);
		this.out.set(false);
	}

	public void crateOut() {
		this.in.set(false);
		this.out.set(true);
	}

	@Override
	protected void initDefaultCommand() {
	}
}
