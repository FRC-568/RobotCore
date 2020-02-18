package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoOne extends SequentialCommandGroup {

	public AutoOne() {
		GearBox gearBox = Robot.getInstance().gearBox;
		addCommands(gearBox.closeCommand(),
			new Drive2017(110 - 24, .3),
			gearBox.openCommand(),
			new WaitCommand(1.5),
			new Drive2017(-10, -.2),
			gearBox.closeCommand());
	}

}
