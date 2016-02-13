package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.MeccanumDrive;

import edu.wpi.first.wpilibj.command.Command;

public class MeccanumDriveManual extends Command {
	public MeccanumDrive drive;

	public MeccanumDriveManual() {

	}

	@Override
	protected void initialize() {
		drive = Robot.getInstance().meccanumDrive;
		requires(drive);
		drive.calibrate();
	}

	@Override
	protected void execute() {
		drive.manualDrive();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
}
