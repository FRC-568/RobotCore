













package frc.team568.robot.crescendo.command;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.crescendo.FieldElements;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.recharge.Constants;

import java.util.function.Supplier;
import java.util.function.DoubleSupplier;



public class ScoreSpeaker extends SequentialCommandGroup {
	private double initTime;

	private Translation3d speaker;

	public ScoreSpeaker(JukeboxSubsystem jukebox, PivotSubsystem pivot, Supplier<Pose2d> robot, boolean red) {
		super();
		speaker = red?FieldElements.redSpeaker:FieldElements.blueSpeaker;
		DoubleSupplier distance = () -> { return Math.hypot(robot.get().getX() - speaker.getX(), robot.get().getY() - speaker.getY()); };
		addCommands(new Aim(pivot, distance), new Shoot(jukebox));
	}
}
