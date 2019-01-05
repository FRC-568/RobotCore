package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.Drive2018;
import org.usfirst.frc.team568.robot.subsystems.BlockHandler;
import org.usfirst.frc.team568.robot.subsystems.BlockLift2018;

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
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .7));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 35));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 56, .4));
				addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(18.5));
				addSequential(robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
				// todo lift arm
				// addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24,
				// .25));
				// todo eject block
			} else {

				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 210, .7));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 190, .8));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 28, .4));
				addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(18.5));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 4, .4));
				addSequential(robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
			}
		} else if (robotPos == 2) {
			if (switchPos == 1) {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24, .4));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 95, .5));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 66, .6));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 6, .4));
				addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(2));
				addSequential(robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));

			} else {
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 92, .6));
				// addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 10));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 6, .4));
				addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(2));
				addSequential(robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));

			}
		} else {
			if (scalePos == 1) {

				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 210, .7));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 190, .8));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 28, .4));
				addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(17));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 4, .4));
				addSequential(robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));

			} else {

				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .6));
				addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), -30));
				addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 56, .4));
				addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(17));
				addSequential(robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
			}

		}
	}
}
