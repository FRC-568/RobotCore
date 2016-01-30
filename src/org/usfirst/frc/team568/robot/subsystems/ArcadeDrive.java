package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.OI;
import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ArcadeDrive extends Subsystem {
	
	protected SpeedController leftFront, leftBack, rightFront, rightBack;
	protected Joystick driveStick;
	//protected Joystick driveRight;
	RobotDrive myDrive;
	
	public void manualDrive(){
		myDrive.arcadeDrive(driveStick);
		Timer.delay(0.01);
		
	}
	
	
	
	
	public void halt(){
		leftFront.set(0);
		leftBack.set(0);;
		rightFront.set(0);;
		rightBack.set(0);
	}
	
	
	
	
	
	public ArcadeDrive(){
		
		leftFront = new Victor(RobotMap.leftFrontMotor);
		leftBack = new Victor(RobotMap.leftBackMotor);
		rightFront = new Victor(RobotMap.rightFrontMotor);
		rightBack = new Victor(RobotMap.rightBackMotor);
		
		leftFront.setInverted(true);
		leftBack.setInverted(true);
		
		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		
		
	}


	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
