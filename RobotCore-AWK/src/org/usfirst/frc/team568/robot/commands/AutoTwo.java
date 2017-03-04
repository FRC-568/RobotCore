package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {
	Shooter shooter;

	public AutoTwo() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive(190, .4));
		addSequential(new Turn(-56));
		// addSequential(new MoveToVisionTarget(Robot.getInstance().driveTrain,
		// Robot.getInstance().gearTracker));
		addSequential(new Drive(135, .4));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new DriveForTime(1, .4, Robot.getInstance().driveTrain));
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new BallShooterandMixer());

	}

}
