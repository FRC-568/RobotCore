package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.stronghold.Robot;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;

import edu.wpi.first.wpilibj.command.Command;

public class ArcadeDriveManual2016 extends Command {
	public final ArcadeDrive arcadeDrive;
	// CrateLifter lifter = Robot.getInstance().crateLifter;

	public ArcadeDriveManual2016() {
		arcadeDrive = Robot.getInstance().arcadeDrive;
		requires(arcadeDrive);

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		arcadeDrive.manualDrive();
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
