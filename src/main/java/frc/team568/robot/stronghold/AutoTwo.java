package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoTwo extends SequentialCommandGroup {
	public AutoTwo(Robot robot) {
		addCommands(
			new AutoShooter(robot.shooter),
			new AutoArm(robot.arms),
			new Drive2016(robot.arcadeDrive));
	}

}
