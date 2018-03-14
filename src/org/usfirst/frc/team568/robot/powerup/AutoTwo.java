package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.commands.Drive2018;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {
	AutoTwo(Robot robot) {
		addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 60, .6));
		// addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 45));
	}
}
