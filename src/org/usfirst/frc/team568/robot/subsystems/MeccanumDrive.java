package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class MeccanumDrive extends Subsystem {
	public final Robot robot;
	protected SpeedController leftFront;
	protected SpeedController leftBack;
	protected SpeedController rightFront;
	protected SpeedController rightBack;
	protected Joystick driveStick;
	RobotDrive myDrive;
	Gyro gyro;

	public MeccanumDrive() {
		robot = Robot.getInstance();

		gyro = new AnalogGyro(0);

		leftFront = new Talon(RobotMap.leftFrontMotor);
		leftBack = new Talon(RobotMap.leftBackMotor);
		rightFront = new Talon(RobotMap.rightFrontMotor);
		rightBack = new Talon(RobotMap.rightBackMotor);

		rightFront.setInverted(true);
		rightBack.setInverted(true);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		driveStick = robot.oi.leftStick;
	}

	public void calibrate() {
		gyro.calibrate();
	}

	public void manualDrive() {
		this.myDrive.mecanumDrive_Cartesian(driveStick.getX(), driveStick.getY(), driveStick.getRawAxis(3), 0);
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
		leftFront.set(0.0);
		leftBack.set(0.0);
		rightFront.set(0.0);
		rightBack.set(0.0);
	}

	@Override
	protected void initDefaultCommand() {
		// setDefaultCommand(new MeccanumDriveManual());
	}
}
