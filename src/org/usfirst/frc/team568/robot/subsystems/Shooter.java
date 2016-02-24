package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.DoNotShoot;
import org.usfirst.frc.team568.robot.commands.GetBall;
import org.usfirst.frc.team568.robot.commands.Shoot;
import org.usfirst.frc.team568.robot.commands.TiltDownwards;
import org.usfirst.frc.team568.robot.commands.TiltUpwards;
import org.usfirst.frc.team568.robot.commands.nudge;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
	SpeedController shooterLeft;
	SpeedController shooterRight;
	SpeedController leftTilt;
	SpeedController rightTilt;
	Servo nudge;
	public DigitalInput upperLimmitSwitch;
	public DigitalInput lowerLimmitSwitch;

	public Shooter() {
		shooterRight = new Victor(RobotMap.shooterRightPort);
		shooterLeft = new Victor(RobotMap.shooterLeftPort);
		shooterRight.setInverted(true);

		leftTilt = new Talon(RobotMap.leftTiltPort);
		rightTilt = new Talon(RobotMap.rightTiltPort);

		nudge = new Servo(RobotMap.nudge);

		upperLimmitSwitch = new DigitalInput(RobotMap.upperLimmitSwitch);
		lowerLimmitSwitch = new DigitalInput(RobotMap.lowerLimmitSwitch);
		// TODO Auto-generated constructor stub
		Robot.getInstance().oi.shootFour.whileHeld(new Shoot());
		Robot.getInstance().oi.shootFive.whileHeld(new GetBall());
		Robot.getInstance().oi.shootSix.whenPressed(new DoNotShoot());
		Robot.getInstance().oi.shootTwo.whenPressed(new TiltDownwards());
		Robot.getInstance().oi.shootThree.whenPressed(new TiltUpwards());
		Robot.getInstance().oi.shootOne.whileHeld(new nudge());

	}

	public void shoot() {
		shooterRight.set(0.65);
		shooterLeft.set(0.65);

	}

	public void nudge() {
		nudge.setAngle(180);
	}

	public void stopnudge() {
		nudge.setAngle(90);
	}

	public void obtainBall() {
		shooterRight.set(-0.20);
		shooterLeft.set(-0.20);
	}

	public void stopShooter() {
		shooterRight.set(0);
		shooterLeft.set(0);
	}

	public void tiltDown() {
		leftTilt.set(-0.5);
		rightTilt.set(-0.5);
	}

	public void tiltUp() {
		leftTilt.set(1);
		rightTilt.set(1);
	}

	public void stopTilt() {
		leftTilt.set(0);
		rightTilt.set(0);
	}

}
