package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.MeccanumDrive;

import edu.wpi.first.wpilibj.command.Command;

public class MeccanumDriveManual extends Command {
	public final MeccanumDrive drive;

	public MeccanumDriveManual() {
		this.drive = Robot.getInstance().meccanumDrive;
		requires(this.drive);
	}

	@Override
	protected void initialize() {
		this.drive.calibrate();
	}

	@Override
	protected void execute() {
		this.drive.manualDrive();
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
