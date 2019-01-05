package org.usfirst.frc.team568.robot.bombbot;

import org.usfirst.frc.team568.robot.steamworks.ControllerButtons;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends IterativeRobot {

	VictorSP leftDrive;
	VictorSP rightDrive;

	VictorSP lowerArm;
	VictorSP upperArm;

	VictorSP claw;

	DifferentialDrive drive;

	double lowerArmPos;
	double upperArmPos;

	double clawPos;

	double leftPower;
	double rightPower;

	private Joystick joystick;

	@Override
	public void robotInit() {

		leftDrive = new VictorSP(1);
		rightDrive = new VictorSP(2);
		leftDrive.setInverted(false);
		rightDrive.setInverted(false);

		lowerArm = new VictorSP(4);
		upperArm = new VictorSP(5);
		claw = new VictorSP(6);

		joystick = new Joystick(0);

		drive = new DifferentialDrive(leftDrive, rightDrive);
	}

	@Override
	public void teleopPeriodic() {
		double x = joystick.getRawAxis(1);
		double y = -joystick.getRawAxis(0);
		x = x * x * Math.signum(x);
		y = y * y * Math.signum(y);
		drive.curvatureDrive(x, y, joystick.getRawButton(ControllerButtons.leftBumper) || (y <= 0.25 && y >= -0.25));

		lowerArm.set(-joystick.getRawAxis(4));
		upperArm.set(joystick.getRawAxis(5));

		claw.set(joystick.getRawAxis(3) - joystick.getRawAxis(2));

	}
}
