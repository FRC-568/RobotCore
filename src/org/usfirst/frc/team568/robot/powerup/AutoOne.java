package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.Drive2018;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {
	public int scalePos;
	public int robotPos;

	public AutoOne(RobotBase robot, int scalePos, int robotPos) {
		this.scalePos = scalePos;
		this.robotPos = robotPos;
		System.out.println("I AM RUNNING AWAY");
		if (robotPos == 1) {
			if (scalePos == 1) {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 304, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				// todo lift arm
				// addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24,
				// .25));
				// todo eject block
			} else {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 216, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));

			}
		} else if (robotPos == 2) {
			if (scalePos == 1) {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 55, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 130, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 205, .25));

			} else {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 55, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 73, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 210, .25));
			}
		} else {
			if (scalePos == 1) {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 216, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));

			} else {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 304, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
			}

		}
	}
}
