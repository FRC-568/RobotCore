package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {

	public AutoTwo() {
		addSequential(new AutoShooter());
		addSequential(new AutoArm());
		addSequential(new Drive());
		// TODO Auto-generated constructor stub
	}

}
