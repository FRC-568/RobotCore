package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {

	public AutoTwo() {

		addSequential(new Drive(190, .4));

		addSequential(new Turn(-56));

		addSequential(new Drive(135, .4));
		// TODO Auto-generated constructor stub
	}

}
