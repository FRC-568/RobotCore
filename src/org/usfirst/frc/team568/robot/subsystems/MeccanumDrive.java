package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.MeccanumDriveManual;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class MeccanumDrive extends Subsystem {
	public final Robot robot;
	protected SpeedController leftFront;
	protected SpeedController leftBack;
	protected SpeedController rightFront;
	protected SpeedController rightBack;
	protected Joystick driveStick;
	RobotDrive myDrive;
	ReferenceFrame2 ref;
	double heading;

	public MeccanumDrive() {
		robot = Robot.getInstance();
		ref = new ReferenceFrame2();

		leftFront = new Talon(RobotMap.leftFrontMotor);
		leftBack = new Talon(RobotMap.leftBackMotor);
		rightFront = new Talon(RobotMap.rightFrontMotor);
		rightBack = new Talon(RobotMap.rightBackMotor);

		rightFront.setInverted(true);
		rightBack.setInverted(true);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		driveStick = robot.oi.leftStick;

	}

	public void manualDrive() {
		if (Math.abs(driveStick.getRawAxis(2)) < .1)
			heading = ref.getHeading();
		else
			heading += driveStick.getRawAxis(2) * 3;
		double error = (ref.getHeading() - heading) * .025;
		myDrive.mecanumDrive_Cartesian(driveStick.getX(), driveStick.getY(), -error, 0);

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
		setDefaultCommand(new MeccanumDriveManual());
	}
}
