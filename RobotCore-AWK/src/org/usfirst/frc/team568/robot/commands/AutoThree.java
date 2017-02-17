package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoThree extends CommandGroup {

	public AutoThree() {

		addSequential(new Drive(206, .4));

		addSequential(new Turn(56));

		addSequential(new Drive(110, .4));
		// TODO Auto-generated constructor stub
	}

}
