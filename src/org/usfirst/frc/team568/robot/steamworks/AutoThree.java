package org.usfirst.frc.team568.robot.steamworks;

import org.usfirst.frc.team568.robot.commands.Drive2017;
import org.usfirst.frc.team568.robot.commands.MoveToVisionTarget;
import org.usfirst.frc.team568.robot.commands.Turn;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoThree extends CommandGroup {

	public AutoThree() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive2017(79, .3));
		addSequential(new Turn(40));
		addSequential(new MoveToVisionTarget(Robot.getInstance().driveTrain, Robot.getInstance().gearTracker));
		addSequential(new Drive2017(48, .3));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new WaitCommand(2));
		addSequential(new Drive2017(-24, -.2));
		addSequential(Robot.getInstance().gearBox.closeCommand());
	}

}
