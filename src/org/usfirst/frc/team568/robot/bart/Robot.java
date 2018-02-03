package org.usfirst.frc.team568.robot.bart;

import org.usfirst.frc.team568.robot.PMW3901;
import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.bart.WestCoastDrive;

import edu.wpi.first.wpilibj.drive.Vector2d;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	public WestCoastDrive driveTrain;
	public PMW3901 godseye;
	
	public Robot() {
		port("leftFrontMotor", 0);
		port("leftBackMotor", 1);
		port("rightFrontMotor", 2);
		port("rightBackMotor", 3);
		
		driveTrain = new WestCoastDrive(this, Talon::new);
		godseye = new PMW3901(SPI.Port.kOnboardCS0);
		godseye.updateInterval = 0.5;
	}
	
	@Override 
	public void teleopInit() {
		godseye.startAutoLoop();
	}
	
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		Vector2d postition = godseye.getPosition();
		System.out.println("X " + postition.x + " Y " + postition.y);
	}
	
	@Override 
	public void disabledInit() {
		godseye.stopAutoLoop();
		
	}

	
}
