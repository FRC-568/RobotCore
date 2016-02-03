package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;

import edu.wpi.first.wpilibj.command.Command;

public class ArcadeDriveManual extends Command {
	public final ArcadeDrive drive;
	// CrateLifter lifter = Robot.getInstance().crateLifter;

	public ArcadeDriveManual() {
		drive = Robot.getInstance().drive;
		requires(drive);

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		drive.manualDrive();
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
