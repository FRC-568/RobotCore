package frc.team568.robot.crescendo.command;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class ScoreSpeaker extends SequentialCommandGroup {

	public ScoreSpeaker(JukeboxSubsystem jukebox, PivotSubsystem pivot, Supplier<Pose2d> robot) {
		super();
		addCommands(new Aim(pivot), new Shoot(jukebox));
	}
}
