package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;

public class ArcadeDriveManual extends Command {
	
	public static final ArcadeDrive arcadeDrive = new ArcadeDrive();
	public ArcadeDriveManual(){
		requires(Robot.arcadeDrive);
		
		
	}

	@Override
	protected void initialize() {
		
		
	}

	@Override
	protected void execute() {
		arcadeDrive.manualDrive();
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
