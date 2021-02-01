package frc.team568.robot.steamworks;

import frc.team568.robot.commands.MoveToVisionTarget;
//import frc.team568.robot.commands.Turn;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoTwo extends SequentialCommandGroup {

	public AutoTwo(Robot robot) {
		addCommands(robot.gearBox.closeCommand(),
			new Drive2017(79, .3, robot.driveTrain, robot.referenceFrame),
			//new Turn(-40), //need generric turn command to replace on adopted by powerup
			new MoveToVisionTarget(robot.driveTrain, robot.gearTracker),
			new Drive2017(48, .3, robot.driveTrain, robot.referenceFrame),
			robot.gearBox.openCommand(),
			new WaitCommand(2),
			new Drive2017(-24, -.2, robot.driveTrain, robot.referenceFrame),
			robot.gearBox.closeCommand());
	}

}
