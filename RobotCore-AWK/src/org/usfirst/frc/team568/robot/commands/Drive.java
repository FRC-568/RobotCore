package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Command {
	DriveTrain drive;
	Timer timer;
	double speed;
	double delay;
	boolean forward;
	double CM;
	ReferenceFrame2 ref;

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		drive = Robot.getInstance().arcadeDrive;
		timer = new Timer();
		CM = SmartDashboard.getNumber("Centimeters",0);
		speed = SmartDashboard.getNumber("speed", 0);
		timer.reset();
		timer.start();

	}

	@Override
	protected void execute() {

	
			drive.forwardWithGyro(speed);		
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		if (ref.ConvertCmtoTicks(CM)>CM){
			return true;
		}
		else{
			return true;
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
