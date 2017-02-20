package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.Shoot;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
	public SpeedController shootMotor;
	public Servo gate;

	public Shooter() {
		shootMotor = new Victor(RobotMap.shooter);
		gate = new Servo(RobotMap.gateServo);

		Robot.getInstance().oi.shoot.whileHeld(new Shoot());

	}

}
