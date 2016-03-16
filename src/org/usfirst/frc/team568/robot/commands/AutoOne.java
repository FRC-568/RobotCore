package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {
	protected SpeedController leftFront, leftBack, rightFront, rightBack;

	public AutoOne() {
		addSequential(new AutoDrive());
		// TODO Auto-generated constructor stub
	}

}
