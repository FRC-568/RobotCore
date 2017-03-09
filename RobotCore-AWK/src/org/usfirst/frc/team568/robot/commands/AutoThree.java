package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoThree extends CommandGroup {

	public AutoThree() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive(300, .4));

		addSequential(new Turn(56));

		addSequential(new MoveToVisionTarget(Robot.getInstance().driveTrain, Robot.getInstance().gearTracker));

		addSequential(new Drive(135, .4));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new DriveForTime(1, .4, Robot.getInstance().driveTrain));
		addSequential(Robot.getInstance().gearBox.closeCommand());
		// addSequential(new BallShooterandMixer());
	}

}
