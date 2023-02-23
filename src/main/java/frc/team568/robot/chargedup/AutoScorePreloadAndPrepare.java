package frc.team568.robot.chargedup;

import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoScorePreloadAndPrepare extends SequentialCommandGroup{
	HashMap eventMap = new HashMap();
	PathPlannerTrajectory path = PathPlanner.loadPath("LeftScorePreloadAndExit", new PathConstraints(4.0, 3.0));
	FollowPathWithEvents command = new FollowPathWithEvents(path,path.getMarkers(),	eventMap);
	AutoScorePreloadAndPrepare(SwerveSubsystem drive, LiftSubsystem lift) {
		addRequirements(drive, lift);
		addCommands(
			new ScorePreload(lift),
			new InstantCommand()
		);
	}
}
