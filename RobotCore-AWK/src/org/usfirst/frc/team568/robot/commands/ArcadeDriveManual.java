package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

public class ArcadeDriveManual extends Command {
	public final DriveTrain arcadeDrive;
	// CrateLifter lifter = Robot.getInstance().crateLifter;

	public ArcadeDriveManual() {
		arcadeDrive = Robot.getInstance().arcadeDrive;
		requires(arcadeDrive);

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		arcadeDrive.arcadeDrive();
		// lifter.lift();
	}

	@Override
	protected boolean isFinished() {
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