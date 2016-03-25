package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.GetBall;
import org.usfirst.frc.team568.robot.commands.Shoot;
import org.usfirst.frc.team568.robot.commands.StopShoot;
import org.usfirst.frc.team568.robot.commands.TiltDownwards;
import org.usfirst.frc.team568.robot.commands.TiltUpwards;
import org.usfirst.frc.team568.robot.commands.nudge;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {
	private final Robot robot;
	public SpeedController shooter;

	public SpeedController leftTilt;
	public SpeedController rightTilt;
	Servo nudge;
	public DigitalInput upperLimmitSwitch;
	public DigitalInput lowerLimmitSwitch;

	public Shooter() {
		robot = Robot.getInstance();

		shooter = new Victor(RobotMap.shooterLeftPort);
		shooter.setInverted(true);

		leftTilt = new Talon(RobotMap.leftTiltPort);
		rightTilt = new Talon(RobotMap.rightTiltPort);
		leftTilt.setInverted(true);

		nudge = new Servo(RobotMap.nudge);

		upperLimmitSwitch = new DigitalInput(RobotMap.upperLimmitSwitch);
		lowerLimmitSwitch = new DigitalInput(RobotMap.lowerLimmitSwitch);
		// TODO Auto-generated constructor stub
		robot.oi.shootFour.whenPressed(new Shoot());
		robot.oi.shootFive.whenPressed(new GetBall());
		// robot.oi.shootEleven.whenPressed(new DoNotShoot());
		robot.oi.shootTwo.whileHeld(new TiltDownwards());
		robot.oi.shootThree.whileHeld(new TiltUpwards());
		robot.oi.shootOne.whenPressed(new nudge());
		robot.oi.shootSix.whenPressed(new StopShoot());

	}

	public void shoot() {

		shooter.set(0.65);
		SmartDashboard.putString("Event:", "Shoot");

	}

	public void nudge() {
		nudge.setAngle(180);
		// SmartDashboard.putString("Event:", "Nudge");
		Timer.delay(.3);

	}

	public void stopnudge() {
		nudge.setAngle(0);
		SmartDashboard.putString("Event:", "Stop Nudge");
	}

	public void obtainBall() {

		shooter.set(-0.325);
		SmartDashboard.putString("Event:", "Get Ball");
	}

	public void stopShooter() {

		shooter.set(0);
		SmartDashboard.putString("Event:", " Stop Shoot");
	}

	public void tiltDown() {

		leftTilt.set(-0.5);
		rightTilt.set(0.5);

		// SmartDashboard.putString("Event:", "Tilt Down");
		Timer.delay(.01);
	}

	public void tiltUp() {

		leftTilt.set(.75);
		rightTilt.set(-.75);

		// System.out.println(SmartDashboard.getNumber("leftTilt"));
		// SmartDashboard.putString("Event:", "Tilt Up");
		Timer.delay(.01);
	}

	public void stopTilt() {
		leftTilt.set(0);
		rightTilt.set(0);
		// SmartDashboard.putString("Event:", "Stop Tilt");
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
