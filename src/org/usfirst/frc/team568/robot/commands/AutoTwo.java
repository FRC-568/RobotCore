package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {
	protected SpeedController leftFront, leftBack, rightFront, rightBack;

	public AutoTwo() {

		addSequential(new ArmDown());
		addSequential(new AutoDrive());

		// TODO Auto-generated constructor stub
	}

}
