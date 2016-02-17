package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.TankDriveManual;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TankDrive extends Subsystem {
	public final Robot robot;
	protected SpeedController leftFront, leftBack, rightFront, rightBack;
	protected Joystick driveStickL;
	protected Joystick driveStickR;
	RobotDrive myRobot;
	SpeedController shooterLeft;
	SpeedController shooterRight;
	SpeedController leftTilt;
	SpeedController rightTilt;

	RobotDrive myDrive;

	public TankDrive() {
		this.robot = Robot.getInstance();

		// leftFront = new Victor(RobotMap.leftFrontMotor);
		// leftBack = new Victor(RobotMap.leftBackMotor);
		// rightFront = new Victor(RobotMap.rightFrontMotor);
		// rightBack = new Victor(RobotMap.rightBackMotor);

		leftFront = new Talon(RobotMap.leftFrontMotor);
		leftBack = new Talon(RobotMap.leftBackMotor);
		rightFront = new Talon(RobotMap.rightFrontMotor);
		rightBack = new Talon(RobotMap.rightBackMotor);

		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		shooterRight = new Victor(RobotMap.shooterRightPort);
		shooterLeft = new Victor(RobotMap.shooterLeftPort);
		shooterRight.setInverted(true);

		leftTilt = new Talon(RobotMap.leftTiltPort);
		rightTilt = new Talon(RobotMap.rightTiltPort);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		driveStickL = robot.oi.leftStick;
		driveStickR = robot.oi.rightStick;
	}

	public void shoot() {
		shooterRight.set(0.75);
		shooterLeft.set(0.75);
	}

	public void obtainBall() {
		shooterRight.set(-0.25);
		shooterLeft.set(-0.25);
	}

	public void stopShooter() {
		shooterRight.set(0);
		shooterLeft.set(0);
	}

	public void tiltUp() {
		leftTilt.set(-0.5);
		rightTilt.set(-0.5);
	}

	public void tiltDown() {
		leftTilt.set(1);
		rightTilt.set(1);
	}

	public void stopTilt() {
		leftTilt.set(0);
		rightTilt.set(0);
	}

	public void manualDrive() {
		myDrive.tankDrive(driveStickL, driveStickR);
		Timer.delay(0.01);
	}

	public void applyPowerToLeftMotors(double speed) {
		leftFront.set(speed);
		leftBack.set(speed);
	}

	public void applyPowerToRightMotors(double speed) {
		rightBack.set(speed);
		rightFront.set(speed);
	}

	public void goForwards(double speed) {
		leftFront.set(speed);
		leftBack.set(speed);
		rightFront.set(speed);
		rightBack.set(speed);
	}

	public void goBackwards(double speed) {
		leftFront.set(speed * -1);
		leftBack.set(speed * -1);
		rightFront.set(speed * -1);
		rightBack.set(speed * -1);
	}

	public void halt() {
		leftFront.set(0);
		leftBack.set(0);
		rightFront.set(0);
		rightBack.set(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new TankDriveManual());
	}

}
