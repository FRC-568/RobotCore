package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

public class Robot extends RobotBase {
	public DriveTrain driveTrain;
	
	public Robot() {
		port("leftFrontMotor", 1);
		port("leftBackMotor", 2);
		port("rightFrontMotor", 3);
		port("rightBackMotor", 4);
		
		driveTrain = new DriveTrain(this);
	}
}
