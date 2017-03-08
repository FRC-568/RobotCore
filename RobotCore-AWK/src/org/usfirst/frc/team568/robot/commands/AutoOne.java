package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne() {
		addSequential(Robot.getInstance().gearBox.closeCommand());

		// addSequential(new MoveToVisionTarget(Robot.getInstance().driveTrain,
		// Robot.getInstance().gearTracker));
		addSequential(new Drive(320, .4));

		// addSequential(new Turn(90));
		//
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new Drive(-50, -.2));
		addSequential(Robot.getInstance().gearBox.closeCommand());
	}

}
