package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne() {
		// addSequential(gearBox.closeCommand());
		addSequential(new Drive(240, .5));
		// addSequential(new Turn(90));
		//
		// addSequential(gearBox.openCommand());
	}

}
