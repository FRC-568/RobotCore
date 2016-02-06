package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTest extends CommandGroup {

	public AutonomousTest(double inches, double speed, double timeOut) {

		// TODO Auto-generated constructor stub
		addSequential(new DriveForwardInInches(inches, speed), timeOut);
	}

	public AutonomousTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
