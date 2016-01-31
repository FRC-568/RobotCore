package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.ArcadeDriveManual;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ArcadeDrive extends Subsystem {
	public final Robot robot;
	protected SpeedController leftFront, leftBack, rightFront, rightBack;
	protected Joystick driveStick;
	// protected Joystick driveRight;
	RobotDrive myDrive;

	public ArcadeDrive() {
		this.robot = Robot.getInstance();

		// leftFront = new Victor(RobotMap.leftFrontMotor);
		// leftBack = new Victor(RobotMap.leftBackMotor);
		// rightFront = new Victor(RobotMap.rightFrontMotor);
		// rightBack = new Victor(RobotMap.rightBackMotor);

		leftFront = new Talon(RobotMap.leftFrontMotor);
		leftBack = new Talon(RobotMap.leftBackMotor);
		rightFront = new Talon(RobotMap.rightFrontMotor);
		rightBack = new Talon(RobotMap.rightBackMotor);

		// leftFront.setInverted(true);
		// leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		driveStick = robot.oi.leftStick;
	}

	public void manualDrive() {
		myDrive.mecanumDrive_Cartesian(driveStick.getX(), driveStick.getY(), driveStick.getRawAxis(3), 0);
		Timer.delay(0.01);
	}

	public void halt() {
		leftFront.set(0);
		leftBack.set(0);
		rightFront.set(0);
		rightBack.set(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveManual());
	}

}
