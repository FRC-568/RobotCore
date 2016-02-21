package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.CrateBot;
import org.usfirst.frc.team568.robot.subsystems.MeccanumDrive;

import edu.wpi.first.wpilibj.command.Command;

public class MeccanumDriveManual extends Command {
	public MeccanumDrive drive;

	public MeccanumDriveManual() {
		drive = CrateBot.getInstance().meccanumDrive;
		requires(drive);

	}

	@Override
	protected void initialize() {
		drive = CrateBot.getInstance().meccanumDrive;

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
