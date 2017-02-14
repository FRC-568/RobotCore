package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoThree extends CommandGroup {

	public AutoThree() {

		addSequential(new Drive(209, .3));

		addSequential(new Turn(53));

		addSequential(new Drive(105, .3));
		// TODO Auto-generated constructor stub
	}

}
