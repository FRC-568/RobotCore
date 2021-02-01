package frc.team568.robot.stronghold;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter2016 extends SubsystemBase {
	public SpeedController shooter;

	public SpeedController leftTilt;
	public SpeedController rightTilt;
	Servo nudge;
	public DigitalInput upperLimmitSwitch;
	public DigitalInput lowerLimmitSwitch;

	public Shooter2016(final RobotBase robot) {
		super(robot);

		shooter = new Victor(port("shooterLeftPort"));
		shooter.setInverted(true);

		leftTilt = new Talon(port("leftTiltPort"));
		rightTilt = new Talon(port("rightTiltPort"));
		leftTilt.setInverted(true);

		nudge = new Servo(port("nudge"));

		upperLimmitSwitch = new DigitalInput(port("upperLimmitSwitch"));
		lowerLimmitSwitch = new DigitalInput(port("lowerLimmitSwitch"));
	}

	public void shoot() {
		shooter.set(0.65);
		SmartDashboard.putString("Event:", "Shoot");
	}

	public void nudge() {
		nudge.setAngle(180);
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
		Timer.delay(.01);
	}

	public void tiltUp() {
		leftTilt.set(.75);
		rightTilt.set(-.75);
		Timer.delay(.01);
	}

	public void stopTilt() {
		leftTilt.set(0);
		rightTilt.set(0);
	}

}
