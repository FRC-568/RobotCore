package frc.team568.robot.stronghold;

import frc.team568.robot.commands.AutoShooter;
import frc.team568.robot.commands.Drive2016;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne() {
		addSequential(new AutoShooter());
		addSequential(new Drive2016());
	}

}
