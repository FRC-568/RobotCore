package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class ScoreSpeaker extends SequentialCommandGroup {

	public ScoreSpeaker(JukeboxSubsystem jukebox, PivotSubsystem pivot) {
		super();
		addCommands(new Aim(pivot), new Shoot(jukebox));
	}
}
