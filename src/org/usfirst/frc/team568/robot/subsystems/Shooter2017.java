package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.steamworks.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Shooter2017 {
	public SpeedController shootMotor;
	public Servo gate;
	public Solenoid ballWranglerIn;
	public Solenoid ballWranglerOut;

	public Shooter2017() {
		shootMotor = new Spark(RobotMap.shooter);
		gate = new Servo(RobotMap.gateServo);
		ballWranglerIn = new Solenoid(RobotMap.ballWranglerIn);
		ballWranglerOut = new Solenoid(RobotMap.ballWranglerOut);

	}

}
