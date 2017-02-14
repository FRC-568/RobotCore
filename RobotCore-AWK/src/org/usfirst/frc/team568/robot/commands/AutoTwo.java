package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {

	public AutoTwo() {

		addSequential(new Drive(220, .5));

		addSequential(new Turn(-53));

		addSequential(new Drive(150, .5));
		// TODO Auto-generated constructor stub
	}

}
