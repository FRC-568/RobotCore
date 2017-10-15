package org.usfirst.frc.team568.robot.stronghold;

import org.usfirst.frc.team568.robot.commands.AutoArm;
import org.usfirst.frc.team568.robot.commands.AutoShooter;
import org.usfirst.frc.team568.robot.commands.Drive2016;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {

	public AutoTwo() {
		addSequential(new AutoShooter());
		addSequential(new AutoArm());
		addSequential(new Drive2016());
		// TODO Auto-generated constructor stub
	}

}
