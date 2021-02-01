package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoOne extends SequentialCommandGroup {

	public AutoOne(Robot robot) {
		GearBox gearBox = robot.gearBox;
		addCommands(gearBox.closeCommand(),
			new Drive2017(110 - 24, .3, robot.driveTrain, robot.referenceFrame),
			gearBox.openCommand(),
			new WaitCommand(1.5),
			new Drive2017(-10, -.2, robot.driveTrain, robot.referenceFrame),
			gearBox.closeCommand());
	}

}
