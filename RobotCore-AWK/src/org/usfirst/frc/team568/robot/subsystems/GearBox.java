package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/* Allen wrote this one
 *
 *
 *
 */
public class GearBox {

	
	public Servo servoL;
	public Servo servoR;
	

	public DigitalInput gearDetector;

	
	public GearBox(){
		servoL = new Servo(RobotMap.servoL);
		servoR = new Servo(RobotMap.servoR);

		gearDetector = new DigitalInput(RobotMap.gearDetector);
		
		Robot.getInstance().oi.openGearBox.whenPressed(this.openCommand());
		Robot.getInstance().oi.closeGearBox.whenPressed(this.closeCommand());
	}

	
	public void open(){
		//servoL.setAngle(0);
		servoR.setAngle(90);

	}

	
	public void close(){
		//servoL.setAngle(0);
		servoR.setAngle(0);
		
	}
	
	public Command openCommand(){
		
		return new Command(){
			@Override
			public void initialize(){
				open();
			}

			@Override
			protected boolean isFinished() {
				// TODO Auto-generated method stub
				return true;
			}
			
		};
		
	}
	
	public Command closeCommand(){
		
		return new Command(){
			@Override
			public void initialize(){
				close();
			}

			@Override
			protected boolean isFinished() {
				// TODO Auto-generated method stub
				return true;
			}
			
		};
		




	}

	public void hasGear() {
		if (gearDetector.get()) {
			SmartDashboard.putBoolean("Has Gear", true);
		} else {
			SmartDashboard.putBoolean("Has Gear", false);
		}
	}

}
