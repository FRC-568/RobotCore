package frc.team568.robot.crescendo.command;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.crescendo.Location;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class ScoreSpeaker extends SequentialCommandGroup {

	private Translation3d speaker;

	public ScoreSpeaker(JukeboxSubsystem jukebox, PivotSubsystem pivot, Supplier<Pose2d> robot, boolean red) {
		super();
		speaker = Location.SPEAKER_TARGET.getTranslation();
		DoubleSupplier distance = () -> { return Math.hypot(robot.get().getX() - speaker.getX(), robot.get().getY() - speaker.getY()); };
		addCommands(new Aim(pivot, distance), new Shoot(jukebox));
	}
}
