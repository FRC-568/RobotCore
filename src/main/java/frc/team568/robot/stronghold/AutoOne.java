package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoOne extends SequentialCommandGroup {

	public AutoOne() {
		addCommands(new AutoShooter(),
			new Drive2016());
	}

}
