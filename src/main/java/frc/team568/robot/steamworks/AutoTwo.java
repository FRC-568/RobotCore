package frc.team568.robot.steamworks;

import frc.team568.robot.commands.Drive2017;
import frc.team568.robot.commands.MoveToVisionTarget;
import frc.team568.robot.commands.Turn;
import frc.team568.robot.subsystems.Shooter2017;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoTwo extends CommandGroup {
	Shooter2017 shooter;

	public AutoTwo() {
		addSequential(Robot.getInstance().gearBox.closeCommand());
		addSequential(new Drive2017(79, .3));
		addSequential(new Turn(-40));
		addSequential(new MoveToVisionTarget(Robot.getInstance().driveTrain, Robot.getInstance().gearTracker));
		addSequential(new Drive2017(48, .3));
		addSequential(Robot.getInstance().gearBox.openCommand());
		addSequential(new WaitCommand(2));
		addSequential(new Drive2017(-24, -.2));
		addSequential(Robot.getInstance().gearBox.closeCommand());

	}

}
