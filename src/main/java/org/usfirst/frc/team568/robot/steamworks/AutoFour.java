package org.usfirst.frc.team568.robot.steamworks;

import org.usfirst.frc.team568.robot.commands.Drive2017;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoFour extends CommandGroup {

	public AutoFour() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive2017(70, .3));

		// I added 20 extra ticks to go forward so get rid of them when you back
		// to gear center auto

		// addSequential(new Turn(90));
		//
		// addSequential(Robot.getInstance().gearBox.openCommand());
		// addSequential(new Drive(50, -.2));
	}

}
