package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.ArcadeDriveManual;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem {
	public final Robot robot;

	protected SpeedController leftFront;
	protected SpeedController leftBack;
	protected SpeedController rightFront;
	protected SpeedController rightBack;
	protected RobotDrive myDrive;

	protected Joystick driveStick1;
	ReferenceFrame2 ref;

	public DriveTrain() {
		this.robot = Robot.getInstance();
		ref = Robot.getInstance().referanceFrame2;

		leftFront = new VictorSP(RobotMap.leftFrontMotor);
		leftBack = new VictorSP(RobotMap.leftBackMotor);
		rightFront = new VictorSP(RobotMap.rightFrontMotor);
		rightBack = new VictorSP(RobotMap.rightBackMotor);

		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);

		driveStick1 = new Joystick(RobotMap.joy1Pos);

	}

	public void arcadeDrive() {

		myDrive.arcadeDrive((driveStick1.getRawAxis(1)), (driveStick1.getRawAxis(4)));

	}

	public void tankDrive() {
		myDrive.tankDrive(driveStick1.getRawAxis(1), driveStick1.getRawAxis(5));
	}

	public void forwardWithGyro(double speed) {
		final double Kp = .156;

		double error = Robot.getInstance().referanceFrame2.getAngle() * Kp;

		if (Robot.getInstance().referanceFrame2.getAngle() <= 1 && Robot.getInstance().referanceFrame2.getAngle() >= -1)
			myDrive.tankDrive(speed, speed, false);
		else
			myDrive.tankDrive(speed - error, speed + error, false);
	}

	public void reverseWithGyro(double speed) {
		final double Kp = .015;
		double error = Robot.getInstance().referanceFrame2.getAngle() * Kp;
		speed = -speed;

		if (Robot.getInstance().referanceFrame2.getAngle() <= 5
				&& Robot.getInstance().referanceFrame2.getAngle() >= -5) {
			myDrive.tankDrive(speed, speed, false);
		} else {
			myDrive.tankDrive(speed - error, speed + error, false);
		}
	}

	public void turnWithGyro(double ra) {
		final double speed = 0.25;
		if (ra < (Robot.getInstance().referanceFrame2.getAngle() + 2)
				|| ra < (Robot.getInstance().referanceFrame2.getAngle() - 2)) {
			myDrive.tankDrive(speed, -speed, false);
		} else if (ra > (Robot.getInstance().referanceFrame2.getAngle() + 2)
				|| ra > (Robot.getInstance().referanceFrame2.getAngle() - 2)) {
			myDrive.tankDrive(-speed, speed, false);
		} else
			halt();
	}

	public void setSpeed(double leftValue, double rightValue) {
		myDrive.tankDrive(leftValue, rightValue);
	}

	public void turnLeft(double speed) {
		myDrive.tankDrive(-speed, speed, false);
	}

	public void turnRight(double speed) {
		myDrive.tankDrive(speed, -speed, false);
	}

	public void goForwards(double speed) {
		myDrive.tankDrive(speed, speed, false);
	}

	public void goBackwards(double speed) {
		myDrive.tankDrive(-speed, -speed, false);
	}

	public void halt() {
		myDrive.stopMotor();
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveManual());
	}

}
