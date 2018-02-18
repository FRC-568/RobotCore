package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.Drive2018;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne(RobotBase robot) {

		addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 48, .25));
	}

}
