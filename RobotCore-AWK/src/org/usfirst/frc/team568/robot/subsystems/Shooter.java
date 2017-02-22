package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Shooter {
	public SpeedController shootMotor;
	public Servo gate;

	public Shooter() {
		shootMotor = new Spark(RobotMap.shooter);
		gate = new Servo(RobotMap.gateServo);

	}

}
