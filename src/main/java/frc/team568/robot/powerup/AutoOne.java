package frc.team568.robot.powerup;

import frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoOne extends SequentialCommandGroup {
	public int scalePos;
	public int robotPos;
	public int switchPos;

	public AutoOne(RobotBase robot, int scalePos, int switchPos, int robotPos) {
		this.scalePos = scalePos;
		this.robotPos = robotPos;
		this.switchPos = switchPos;

		System.out.println("I AM RUNNING AWAY");
		if (robotPos == 1) {
			if (scalePos == 1) {
				addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .7),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), 35),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 56, .4),
					robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(18.5),
					robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
					// todo lift arm
					// addSequential(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24,
					// .25));
					// todo eject block
			} else {
				addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 210, .7),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 190, .8),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 28, .4),
					robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(18.5),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 4, .4),
					robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
			}
		} else if (robotPos == 2) {
			if (switchPos == 1) {
				addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 24, .4),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 95, .5),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 66, .6),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 6, .4),
					robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(2),
					robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
			} else {
				addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 92, .6),
					//new Turn2018(robot.getSubsystem(DriveTrain2018.class), 10),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 6, .4),
					robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(2),
					robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
			}
		} else {
			if (scalePos == 1) {
				addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 210, .7),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), -90),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 190, .8),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 28, .4),
					robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(17),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 4, .4),
					robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));

			} else {
				addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 195, .6),
					new Turn2018(robot.getSubsystem(DriveTrain2018.class), -30),
					new Drive2018(robot.getSubsystem(DriveTrain2018.class), 56, .4),
					robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(17),
					robot.getSubsystem(BlockHandler.class).getCommandBlockLiftOut(2));
			}

		}
	}
}
