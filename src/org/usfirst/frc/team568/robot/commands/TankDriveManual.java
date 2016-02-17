package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.Command;

public class TankDriveManual extends Command {
	public final TankDrive tankDrive;
	// CrateLifter lifter = Robot.getInstance().crateLifter;

	public TankDriveManual() {
		tankDrive = Robot.getInstance().tankDrive;
		requires(tankDrive);

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		tankDrive.manualDrive();
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
