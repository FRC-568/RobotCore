package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.commands.ArcadeDriveManual;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class MeccanumDrive extends Subsystem {
	public final Robot robot;
	protected SpeedController leftFront;
	protected SpeedController leftBack;
	protected SpeedController rightFront;
	protected SpeedController rightBack;
	protected Joystick driveStick;
	RobotDrive myDrive;
	Gyro gyro;

	public MeccanumDrive() {
		this.robot = Robot.getInstance();

		this.gyro = new AnalogGyro(0);

		this.leftFront = new Talon(0);
		this.leftBack = new Talon(1);
		this.rightFront = new Talon(2);
		this.rightBack = new Talon(4);

		this.rightFront.setInverted(true);
		this.rightBack.setInverted(true);

		this.myDrive = new RobotDrive(this.leftFront, this.leftBack, this.rightFront, this.rightBack);
		this.driveStick = this.robot.oi.leftStick;
	}

	public void calibrate() {
		this.gyro.calibrate();
	}

	public void manualDrive() {
		this.myDrive.mecanumDrive_Cartesian(this.driveStick.getX(), this.driveStick.getY(),
				this.driveStick.getRawAxis(3), 0.0D);
		Timer.delay(0.01D);
	}

	public void halt() {
		this.leftFront.set(0.0D);
		this.leftBack.set(0.0D);
		this.rightFront.set(0.0D);
		this.rightBack.set(0.0D);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveManual());
	}
}
