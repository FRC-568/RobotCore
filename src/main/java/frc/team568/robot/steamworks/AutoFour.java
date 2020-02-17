package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoFour extends SequentialCommandGroup {

	public AutoFour() {
		addCommands(Robot.getInstance().gearBox.closeCommand(),
			new Drive2017(70, .3));

		// I added 20 extra ticks to go forward so get rid of them when you back
		// to gear center auto

		// addSequential(new Turn(90));
		//
		// addSequential(Robot.getInstance().gearBox.openCommand());
		// addSequential(new Drive(50, -.2));
	}

}
