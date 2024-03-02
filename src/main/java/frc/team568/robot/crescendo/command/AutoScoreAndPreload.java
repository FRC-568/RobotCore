package frc.team568.robot.crescendo.command;

import java.util.HashMap;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.util.Units;

import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;

import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

public class AutoScoreAndPreload extends SequentialCommandGroup {

	PathPlannerPath path = PathPlannerPath.fromPathFile("LeftScorePreloadAndExit");
	PathConstraints constraints = new PathConstraints(
        3.0, 4.0,
        Units.degreesToRadians(540), Units.degreesToRadians(720));

	HashMap<String, Command> eventMap = new HashMap<>();

	Command autoToSpeaker = AutoBuilder.pathfindThenFollowPath(
        path,
        constraints,
        3.0
	);

	public AutoScoreAndPreload(SwerveSubsystem drive, JukeboxSubsystem juke, PivotSubsystem pivot) {
		addRequirements(drive,juke,pivot);
		addCommands(
			autoToSpeaker, // go to place
			new ScoreSpeaker(juke, pivot), // go down
			new Intake(juke, pivot) // start intake	
		);
	}
}
