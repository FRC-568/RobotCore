package frc.team568.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PID {
	double Pan;
	double Tilt;
	double BoxSize;
	double P;
	double I;
	double D;
	double KI;
	double KP;
	double KD;
	double TiltKP;
	double TiltKD;
	double TiltKI;
	double ErrSum;
	double Err2;
	double Err;
	double Pow;
	boolean LL;
	boolean LR;
	double tiltErr;
	double tiltErr2;
	double tiltPow;
	Joystick joystick1;

public void pidtest(){
	joystick1 = new Joystick(0);
	TiltKP = SmartDashboard.getNumber("TP", 5.0);
	TiltKI = SmartDashboard.getNumber("TI", 0.0);
	TiltKD = SmartDashboard.getNumber("TD", 0.0);
	KP = SmartDashboard.getNumber("P", 2.0);
	KI = SmartDashboard.getNumber("I", 0.7);
	KD = SmartDashboard.getNumber("D", 0.0);

	NetworkTable visionTable = NetworkTableInstance.getDefault().getTable("SmartDashboard");
	try
	{
		System.out.println(visionTable.getEntry("centerX").getNumber(0.0));
		System.out.println(visionTable.getEntry("centerY").getNumber(0.0));
		System.out.println(visionTable.getEntry("area").getNumber(0.0));
		SmartDashboard.putNumber("P", 2.00);
		SmartDashboard.putNumber("I", 0.700);
		SmartDashboard.putNumber("D", 0);
		SmartDashboard.putNumber("TP", 5.000);
		SmartDashboard.putNumber("TI", 0);
		SmartDashboard.putNumber("TD", 0);
	}
	
	catch(Exception ex)
	{
		
	}
	
	if(joystick1.getRawButton(3))
	{
		
	 Pan = SmartDashboard.getNumber("centerX", 0.0);
	 Tilt = SmartDashboard.getNumber("centerY", 0.0);
	 BoxSize = SmartDashboard.getNumber("COG_BOX_SIZE", 0.0);
	 if (Pan > 0) { 
		 Err2 = Err;
		 Err = Pan - 320; ErrSum += Err; if (ErrSum > 1000) ErrSum = 1000; 
		 if (ErrSum < -1000) ErrSum = -1000;
		 Pow = Err * (KP / 1000) + Pow * (KI / 1000) + (Err - Err2) * (KD / 1000);// Pos // turn Robot.tankDrive(-Pow, Pow); if
	 if (Err < 0) { 
	LL = false;
	LR = true; 
	}
	else {
		LL = true;
		LR = false;
		}
	 SmartDashboard.putNumber("ERR", Err);
	 SmartDashboard.putNumber("Pow",Pow);
	 SmartDashboard.putNumber("Size", BoxSize);
	 
	 if (Math.abs(Err) < 100) { // ErrSum = 0; if (BoxSize < 100)
	// DriveTrain.tankDrive(-.5, -.5);
	 
	 tiltErr = 240 - Tilt;
	 tiltErr2 = tiltErr;
	 tiltPow = tiltErr * (TiltKP / 1000) + tiltPow * (TiltKI / 1000)+ (tiltErr - tiltErr2) * (TiltKD / 1000);
	 //bob.set(tiltPow); 
	 //sam.set(tiltPow);
	 SmartDashboard.putNumber("TiltPow", tiltPow);
	  
	  }
	  
	 
	 
	  else { 
		 // bob.set(0);
		  //sam.set(0);
		  if (LL) {
			 // DarwinsRobot.tankDrive(-.55, .55); 
			  } 
		  if (LR) { /*DriveTrain.tankDrive(.55, -.55);*/ } }
	 }

	}
	}
}

