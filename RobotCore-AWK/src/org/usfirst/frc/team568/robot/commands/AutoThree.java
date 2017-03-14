package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoThree extends CommandGroup {

	public AutoThree() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive(79, .3));
		addSequential(new Turn(40));
		addSequential(new MoveToVisionTarget(Robot.getInstance().driveTrain, Robot.getInstance().gearTracker));
		addSequential(new Drive(48, .3));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new WaitCommand(2));
		addSequential(new Drive(-24, -.2));
		addSequential(Robot.getInstance().gearBox.closeCommand());
	}

}
