package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoOne extends CommandGroup {

	public AutoOne() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive(320, .2));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new WaitCommand(1));
		addSequential(new Drive(-100, -.2));
		addSequential(Robot.getInstance().gearBox.closeCommand());
	}

}
