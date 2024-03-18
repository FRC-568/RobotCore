package frc.team568.robot.crescendo.command;

import static frc.team568.robot.crescendo.Constants.SwerveConstants.kPathFollowerConstraints;

import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.team568.robot.crescendo.Location;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;

public class CommandFactory  {
	private final SwerveSubsystem drive;
	private final PivotSubsystem pivot;

	public CommandFactory(final SwerveSubsystem drive, final PivotSubsystem pivot) {
		this.drive = drive;
		this.pivot = pivot;
	}

	public Command aimAtSpeaker() {
		return aimAtLocation(Location.SPEAKER_TARGET);
	}

	public Command aimAtAmp() {
		return aimAtLocation(Location.AMP_TARGET);
	}

	public Command aimAtLocation(final Location target) {
		var driveTranslation = drive.getPose().getTranslation();
		var targetTranslation = target.getTranslation().toTranslation2d().minus(driveTranslation);

		var driveCommand = rotateToAngle(targetTranslation.getAngle());
		var pivotCommand = new Aim(pivot, targetTranslation::getNorm);
		return Commands.parallel(driveCommand, pivotCommand);
	}

	public Command rotateToAngle(Rotation2d angle) {
		var driveTranslation = drive.getPose().getTranslation();
		return AutoBuilder.pathfindToPose(new Pose2d(driveTranslation, angle), kPathFollowerConstraints);
	}

	public Command goToNoteOne() {
		String pathName;
		double yCoordinate = drive.getPose().getY();
		if (yCoordinate >= 6.15)
			pathName = "S1-N1";
		else if (yCoordinate <= 5.0)
			pathName = "S3-N1";
		else
			pathName = "S2-N1";

		return AutoBuilder.pathfindThenFollowPath(PathPlannerPath.fromPathFile(pathName), kPathFollowerConstraints);
	}

	public Command dynamicWait(DoubleSupplier timeSupplier) {
		return Commands.defer(() -> Commands.waitSeconds(timeSupplier.getAsDouble()), null);
	}
}
