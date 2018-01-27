package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	public WestCoastDrive driveTrain;
	
	public Robot() {
		port("leftFrontMotor", 0);
		port("leftBackMotor", 1);
		port("rightFrontMotor", 2);
		port("rightBackMotor", 3);
		
		driveTrain = new WestCoastDrive(this, Talon::new);
	}
	
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}
}
