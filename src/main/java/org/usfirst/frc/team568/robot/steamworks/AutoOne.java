package org.usfirst.frc.team568.robot.steamworks;

import org.usfirst.frc.team568.robot.commands.Drive2017;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoOne extends CommandGroup {

	public AutoOne() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive2017(110 - 24, .3));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new WaitCommand(1.5));
		addSequential(new Drive2017(-10, -.2));
		addSequential(Robot.getInstance().gearBox.closeCommand());
	}

}
