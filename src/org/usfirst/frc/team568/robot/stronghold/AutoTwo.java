package org.usfirst.frc.team568.robot.stronghold;

import org.usfirst.frc.team568.robot.commands.AutoShooter;
import org.usfirst.frc.team568.robot.commands.Drive2016;
import org.usfirst.frc.team568.robot.subsystems.Arms;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoTwo extends CommandGroup {
	public AutoTwo(Arms arm) {
		addSequential(new AutoShooter());
		addSequential(new AutoArm(arm));
		addSequential(new Drive2016());
	}

}
