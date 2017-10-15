package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.commands.ArcadeDriveManual2016;
import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.stronghold.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ArcadeDrive extends Subsystem {
	public final Robot robot;

	protected SpeedController leftFront, leftBack, rightFront, rightBack;

	protected Joystick driveStickL;
	protected Joystick driveStickR;

	RobotDrive myRobot;
	RobotDrive myDrive;

	ReferenceFrame2016 ref;

	static double sHeading;
	double Kp;

	public ArcadeDrive() {
		this.robot = Robot.getInstance();
		ref = Robot.getInstance().referanceFrame2;
		// sHeading = ref.getHeading();

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
		driveStickL = robot.oi.joyStick1;
		driveStickR = robot.oi.joyStick3;

	}

	public void manualDrive() {
		if (!leftFront.getInverted() || !leftBack.getInverted()) {
			leftFront.setInverted(true);
			leftBack.setInverted(true);
		}

		if (robot.oi.trigger.get()) {
			myDrive.arcadeDrive(driveStickL);
		} else {
			halt();
		}
		Timer.delay(0.01);
	}

	public void forwardWithGyro(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		Kp = .015;
		double error = Robot.getInstance().referanceFrame2.getAngle() * Kp;

		if (Robot.getInstance().referanceFrame2.getAngle() <= 5
				&& Robot.getInstance().referanceFrame2.getAngle() >= -5) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(speed);
			rightBack.set(speed);
		} else {
			System.out.println(error);
			leftFront.set(speed - error);
			leftBack.set(speed - error);
			rightFront.set(speed + error);
			rightBack.set(speed + error);
		}

	}

	public void reverseWithGyro(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		Kp = .015;
		double error = Robot.getInstance().referanceFrame2.getAngle() * Kp;
		speed = -speed;

		if (Robot.getInstance().referanceFrame2.getAngle() <= 5
				&& Robot.getInstance().referanceFrame2.getAngle() >= -5) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(speed);
			rightBack.set(speed);
		} else {
			System.out.println(error);
			leftFront.set(speed - error);
			leftBack.set(speed - error);
			rightFront.set(speed + error);
			rightBack.set(speed + error);
		}
	}

	public void rightWithGyro(double degrees, double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		double ra = ref.getAngle() + degrees;
		if (ref.getAngle() != ra) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(-speed);
			rightBack.set(-speed);
		}
	}

	public void leftWithGyro(double degrees, double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		double ra = ref.getAngle() - degrees;
		if (ref.getAngle() != ra) {
			leftFront.set(-speed);
			leftBack.set(-speed);
			rightFront.set(speed);
			rightBack.set(speed);
		}

	}

	public void turnLeft(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftFront.set(-speed);
		leftBack.set(-speed);
		rightFront.set(speed);
		rightBack.set(speed);

	}

	public void turnRight(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftBack.set(speed);
		leftFront.set(speed);
		rightBack.set(-speed);
		rightFront.set(-speed);

	}

	public void goForwards(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftBack.set(speed);
		leftFront.set(speed);
		rightBack.set(speed);
		rightFront.set(speed);

	}

	public void goBackwards(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftBack.set(-speed);
		leftFront.set(-speed);
		rightBack.set(-speed);
		rightFront.set(-speed);

	}

	public void halt() {
		leftFront.set(0);
		leftBack.set(0);
		rightFront.set(0);
		rightBack.set(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveManual2016());
	}

}
