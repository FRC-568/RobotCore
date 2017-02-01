package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearBox {
	
	public Servo servo1;
	public Servo servo2;
	
	public DigitalInput gearDetector;
	
	public GearBox(){
		servo1 = new Servo(RobotMap.servo1);
		servo2 = new Servo(RobotMap.servo2);
		gearDetector = new DigitalInput(RobotMap.gearDetector);
	}
	
	public void open(){
		servo1.setAngle(10);
		servo2.setAngle(10);
	}
	
	public void close(){
		servo1.setAngle(50);
		servo2.setAngle(50);
		
	}
	
	public void hasGear(){
		if(gearDetector.get()){
			SmartDashboard.putBoolean("Has Gear", true);
		}else{
			SmartDashboard.putBoolean("Has Gear", false);
		}
	}

}
