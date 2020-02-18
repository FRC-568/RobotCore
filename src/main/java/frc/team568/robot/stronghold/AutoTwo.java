package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoTwo extends SequentialCommandGroup {
	public AutoTwo(Arms arm) {
		addCommands(new AutoShooter(),
			new AutoArm(arm),
			new Drive2016());
	}

}
