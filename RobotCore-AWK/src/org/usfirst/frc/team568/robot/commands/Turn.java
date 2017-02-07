package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turn extends Command {
	DriveTrain drive;
	double degrees;
	double ra;
	ADIS16448_IMU imu;
public Turn(double degrees){
	this.degrees = degrees;
	
}
public Turn(){
	degrees = SmartDashboard.getNumber("Degrees", 90);
}
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		drive = Robot.getInstance().driveTrain;
		imu = Robot.getInstance().imu;
		ra = Robot.getInstance().imu.getAngle() + degrees;
		
		if(degrees > 0){
			drive.turnRight(.5);
		}else if(degrees < 0){
			drive.turnLeft(.5);
		}else{
			drive.halt();
		}
			

	}

	@Override
	protected void execute() {
		

		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (degrees> 0){
			return (ra - imu.getAngle() <=  2);
		}
		else{
			return (ra - imu.getAngle() >= -2);
		}
			
	}

	@Override
	protected void end() {
		drive.halt();
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
