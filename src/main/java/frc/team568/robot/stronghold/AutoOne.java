package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoOne extends SequentialCommandGroup {

	public AutoOne(Robot robot) {
		addCommands(
			new AutoShooter(robot.shooter),
			new Drive2016(robot.arcadeDrive));
	}

}
