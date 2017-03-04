package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive(800, .4));
		// addSequential(new Turn(90));
		//
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new DriveForTime(1, .4, Robot.getInstance().driveTrain));
	}

}
