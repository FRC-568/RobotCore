package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTest extends CommandGroup {

	public AutonomousTest(double speed, double inches) {

		// TODO Auto-generated constructor stub
		addSequential(new DriveForwardInInches(speed, inches));
	}

}
