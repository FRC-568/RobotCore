package frc.team568.robot.chargedup;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoScorePreloadAndPrepare extends SequentialCommandGroup{
	PathPlannerTrajectory path = PathPlanner.loadPath("LeftScorePreloadAndExit", new PathConstraints(4.0, 3.0));
	AutoScorePreloadAndPrepare(SwerveSubsystem drive, LiftSubsystem lift) {
		addRequirements(drive, lift);
		addCommands(
			new ScorePreload(lift),
			new InstantCommand()
		);
	}
}
