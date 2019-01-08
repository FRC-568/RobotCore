package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;
import frc.team568.robot.commands.GetBall;
import frc.team568.robot.commands.Shoot2016;
import frc.team568.robot.commands.StopShoot;
import frc.team568.robot.commands.TiltDownwards;
import frc.team568.robot.commands.TiltUpwards;
import frc.team568.robot.commands.Nudge;
import frc.team568.robot.stronghold.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter2016 extends SubsystemBase {
	private final Robot myrobot;
	public SpeedController shooter;

	public SpeedController leftTilt;
	public SpeedController rightTilt;
	Servo nudge;
	public DigitalInput upperLimmitSwitch;
	public DigitalInput lowerLimmitSwitch;

	public Shooter2016(final RobotBase robot) {
		super(robot);
		this.myrobot = Robot.getInstance();

		shooter = new Victor(port("shooterLeftPort"));
		shooter.setInverted(true);

		leftTilt = new Talon(port("leftTiltPort"));
		rightTilt = new Talon(port("rightTiltPort"));
		leftTilt.setInverted(true);

		nudge = new Servo(port("nudge"));

		upperLimmitSwitch = new DigitalInput(port("upperLimmitSwitch"));
		lowerLimmitSwitch = new DigitalInput(port("lowerLimmitSwitch"));
		this.myrobot.oi.shootFour.whenPressed(new Shoot2016());
		this.myrobot.oi.shootFive.whenPressed(new GetBall());
		this.myrobot.oi.shootTwo.whileHeld(new TiltDownwards());
		this.myrobot.oi.shootThree.whileHeld(new TiltUpwards());
		this.myrobot.oi.shootOne.whenPressed(new Nudge());
		this.myrobot.oi.shootSix.whenPressed(new StopShoot());
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
		// SmartDashboard.putString("Event:", "Tilt Up");
		Timer.delay(.01);
	}

	public void stopTilt() {
		leftTilt.set(0);
		rightTilt.set(0);
		// SmartDashboard.putString("Event:", "Stop Tilt");
	}

}
