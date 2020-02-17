package frc.team568.robot.powerup;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoTwo extends SequentialCommandGroup {
	AutoTwo(Robot robot) {
		addCommands(new Drive2018(robot.getSubsystem(DriveTrain2018.class), 125, .6));
		// addSequential(new Turn2018(robot.getSubsystem(DriveTrain2018.class), 90));
		// addSequential(robot.getSubsystem(BlockLift2018.class).getCommandMoveLiftTo(18.5));

	}
}
