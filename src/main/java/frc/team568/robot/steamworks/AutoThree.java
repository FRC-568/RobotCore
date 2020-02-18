package frc.team568.robot.steamworks;

import frc.team568.robot.commands.MoveToVisionTarget;
//import frc.team568.robot.commands.Turn;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoThree extends SequentialCommandGroup {

	public AutoThree() {
		GearBox gearBox = Robot.getInstance().gearBox;
		addCommands(gearBox.closeCommand(),
			new Drive2017(79, .3),
			//new Turn(40), //need generic turn command to replace one adopted by powerup
			new MoveToVisionTarget(Robot.getInstance().driveTrain, Robot.getInstance().gearTracker),
			new Drive2017(48, .3),
			gearBox.openCommand(),
			new WaitCommand(2),
			new Drive2017(-24, -.2),
			gearBox.closeCommand());
	}

}
