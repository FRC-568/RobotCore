package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne() {
	
		addSequential(new Drive());
		// TODO Auto-generated constructor stub
	}

}
