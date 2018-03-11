package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.Drive2018;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {
	public int scalePos;
	public int robotPos;
	public int switchPos;

	AutoOne(RobotBase robot) {
		// addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
	}

	public AutoOne(RobotBase robot, int scalePos, int switchPos, int robotPos) {
		this.scalePos = scalePos;
		this.robotPos = robotPos;
		this.switchPos = switchPos;

		System.out.println("I AM RUNNING AWAY");
		if (robotPos == 1) {
			if (scalePos == 1) {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .3));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 25));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 55, .3));
				// todo lift arm
				// addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24,
				// .25));
				// todo eject block
			} else {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 216, .4));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 190, .4));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24, .3));
			}
		} else if (robotPos == 2) {
			if (switchPos == 1) {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24, .3));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 95, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 72, .25));

			} else {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 10));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 74, .25));

			}
		} else {
			if (scalePos == 1) {

				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 160, .25));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 62, .3));

			} else {

				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .3));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -20));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 65, .3));
			}

		}
	}
}
