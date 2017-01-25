package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoThree extends CommandGroup {

		public AutoThree() {
		
			addSequential(new Drive());
			addSequential(new Turn());
			addSequential(new Drive());
			addSequential(new Turn());
			addSequential(new Drive());
			// TODO Auto-generated constructor stub
		}

	
}
