// package frc.team568.robot.chargedup;

// import java.util.HashMap;

// import com.pathplanner.lib.PathConstraints;
// import com.pathplanner.lib.PathPlanner;
// import com.pathplanner.lib.PathPlannerTrajectory;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// public class AutoScorePreloadAndPrepare extends SequentialCommandGroup{
// 	RobotContainer builder;
// 	PathPlannerTrajectory path = PathPlanner.loadPath("LeftScorePreloadAndExit", new PathConstraints(4.0, 3.0));
// 	HashMap<String, Command> eventMap = new HashMap<>();
// 	Command fullAuto = builder.autoBuilder.fullAuto(path);

// 	AutoScorePreloadAndPrepare(SwerveSubsystem drive, LiftSubsystem lift) {
// 		addRequirements(drive, lift);
// 		addCommands(
// 			new ScorePreload(lift),
// 			fullAuto
// 		);
// 	}
// }
