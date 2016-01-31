package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.GreenHornDefaultCommands;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GreenHorn extends Subsystem {
	public final Robot robot;
	SpeedController leftMotor;
	SpeedController rightMotor;
	SpeedController lowGoal;
	Solenoid aimerUp;
	Solenoid aimerDown;
	Servo nudger;

	public GreenHorn() {
		this.robot = Robot.getInstance();
		leftMotor = new Talon(RobotMap.leftShooterMotor);
		leftMotor.setInverted(true);
		rightMotor = new Talon(RobotMap.rightShooterMotor);
		lowGoal = new Talon(RobotMap.lowZoneMotor);
		aimerUp = new Solenoid(RobotMap.aimerUp);
		aimerDown = new Solenoid(RobotMap.aimerDown);
		nudger = new Servo(RobotMap.nudge);
	}

	public void nudgerNeutral() {
		nudger.set(180);
	}

	public void obtainBall(double speed) {
		nudger.setAngle(90);
		setSpeed(speed * -1);
	}

	public void shootBall(double speed) {
		nudger.setAngle(180);
		setSpeed(speed);
	}

	public void setSpeed(double speed) {
		leftMotor.set(speed);
		rightMotor.set(speed);
	}

	public void goToPositionShootHigh() {
		aimerUp.set(true);
		aimerDown.set(false);
	}

	public void goToPositionShootLow(double speed) {
		lowGoal.set(speed);
		aimerUp.set(true);
		aimerDown.set(false);
	}

	public void goToPositionLoad() {
		aimerDown.set(true);
		aimerUp.set(false);
	}

	public void manuallySetPowerForGettingBall(double speed) {
		nudger.setAngle(90);
		setSpeed(-1 * speed);
	}

	public void manuallySetPowerForShooting(double speed) {
		nudger.setAngle(180);
		setSpeed(speed);
	}

	public void stopEveryThing() {
		nudger.setAngle(180);
		setSpeed(0);

	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new GreenHornDefaultCommands());
	}

}
