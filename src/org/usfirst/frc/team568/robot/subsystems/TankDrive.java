package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.TankDriveManual;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TankDrive extends Subsystem {
	public final Robot robot;
	protected SpeedController leftFront, leftBack, rightFront, rightBack;
	protected Joystick driveStickL;
	protected Joystick driveStickR;
	RobotDrive myRobot;
	RobotDrive myDrive;
	ReferenceFrame2 ref;
	static double sHeading;
	static double Kp = .002;

	public TankDrive() {
		this.robot = Robot.getInstance();
		ref = new ReferenceFrame2();
		sHeading = ref.getHeading();

		// leftFront = new Victor(RobotMap.leftFrontMotor);
		// leftBack = new Victor(RobotMap.leftBackMotor);
		// rightFront = new Victor(RobotMap.rightFrontMotor);
		// rightBack = new Victor(RobotMap.rightBackMotor);

		leftFront = new VictorSP(RobotMap.leftFrontMotor);
		leftBack = new VictorSP(RobotMap.leftBackMotor);
		rightFront = new VictorSP(RobotMap.rightFrontMotor);
		rightBack = new VictorSP(RobotMap.rightBackMotor);

		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		driveStickL = robot.oi.leftStick;
		driveStickR = robot.oi.rightStick;
	}

	public void manualDrive() {
		if (Robot.getInstance().oi.leftTrigger.get()) {
			myDrive.tankDrive(driveStickL, driveStickR);
		} else if (Robot.getInstance().oi.rightTrigger.get()) {
			myDrive.tankDrive(driveStickL, driveStickR);
		} else {
			myDrive.tankDrive(0, 0);
		}
		Timer.delay(0.01);

	}

	public void forwardWithGyro(double speed) {
		double error = ref.getAngle() * Kp;

		if (ref.getAngle() <= 5 && ref.getAngle() >= -5) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(speed);
			rightBack.set(speed);
		} else {
			leftFront.set(speed - error);
			leftBack.set(speed - error);
			rightFront.set(speed + error);
			rightBack.set(speed + error);
		}

	}

	public void rightWithGyro(double degrees, double speed) {
		double ra = ref.getAngle() + degrees;
		if (ref.getAngle() != ra) {
			leftFront.set(-speed);
			leftBack.set(-speed);
			rightFront.set(speed);
			rightBack.set(speed);
		}
	}

	public void leftWithGyro(double degrees, double speed) {
		double ra = ref.getAngle() + degrees;
		if (ref.getAngle() != ra) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(-speed);
			rightBack.set(-speed);
		}

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
