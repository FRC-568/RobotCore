package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {

	public AutoTwo() {

		addSequential(new Drive());
		addSequential(new Turn());
		// TODO Auto-generated constructor stub
	}

}
