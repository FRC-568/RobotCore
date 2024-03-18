package frc.team568.robot.crescendo.command;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.crescendo.FieldElements;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.crescendo.command.Aim;

public class ScoreSpeaker extends SequentialCommandGroup {

	private Translation3d speaker;

	public ScoreSpeaker(JukeboxSubsystem jukebox, PivotSubsystem pivot, Supplier<Pose2d> robot, boolean red) {
		speaker = red?FieldElements.redSpeaker:FieldElements.blueSpeaker;
		DoubleSupplier distance = () -> { return Math.hypot(robot.get().getX() - speaker.getX(), robot.get().getY() - speaker.getY()); };
		addCommands(new Aim(pivot), new Shoot(jukebox));
	}
	public ScoreSpeaker(JukeboxSubsystem jukebox, PivotSubsystem pivot, double angle) {
		addCommands(new InstantCommand(() -> pivot.setAngle(angle)), new Shoot(jukebox));
	}
}
