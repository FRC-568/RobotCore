package frc.team568.robot.steamworks;

import frc.team568.robot.commands.MoveToVisionTarget;
//import frc.team568.robot.commands.Turn;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoTwo extends SequentialCommandGroup {
	Shooter2017 shooter;

	public AutoTwo() {
		addCommands(Robot.getInstance().gearBox.closeCommand(),
			new Drive2017(79, .3),
			//new Turn(-40), //need generric turn command to replace on adopted by powerup
			new MoveToVisionTarget(Robot.getInstance().driveTrain, Robot.getInstance().gearTracker),
			new Drive2017(48, .3),
			Robot.getInstance().gearBox.openCommand(),
			new WaitCommand(2),
			new Drive2017(-24, -.2),
			Robot.getInstance().gearBox.closeCommand());

	}

}
