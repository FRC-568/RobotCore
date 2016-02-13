package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;

import edu.wpi.first.wpilibj.command.Command;

public class ArcadeDriveManual extends Command {
	public final ArcadeDrive ArcadeDrive;
	// CrateLifter lifter = Robot.getInstance().crateLifter;

	public ArcadeDriveManual() {
		ArcadeDrive = Robot.getInstance().arcadeDrive;
		requires(ArcadeDrive);

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		ArcadeDrive.manualDrive();
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
