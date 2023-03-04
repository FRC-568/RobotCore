package frc.team568.robot.chargedup;

import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

final class RobotContainer {
	CommandXboxController controller1;
	final SwerveSubsystem drive;
	// final LiftSubsystem lift;
	HashMap<String, Command> eventMap = new HashMap<>();
	SwerveAutoBuilder autoBuilder;

	// Auto tab objects
	private ShuffleboardTab autoTab;
	private SendableChooser<String> programChooser;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));

		// lift = new LiftSubsystem(0, 0, 0, 0);

		configureButtonBindings();

		autoBuilder = new SwerveAutoBuilder(
				drive::getPose, // Pose2d supplier
				drive::resetPose, // Pose2d consumer, used to reset odometry at the beginning of auto
				drive.getKinematics(), // SwerveDriveKinematics
				new PIDConstants(5.0, 0.0, 0.0), // PID constants to correct for translation error (used to create the X and Y PID controllers)
				new PIDConstants(0.5, 0.0, 0.0), // PID constants to correct for rotation error (used to create the rotation controller)
				drive::setModuleStates, // Module states consumer used to output to the drive subsystem
				eventMap,
				true, // Should the path be automatically mirrored depending on alliance color. Optional, defaults to true
				drive // The drive subsystem. Used to properly set the requirements of path following commands
			);

		setupAutoTab();
	}

	public void configureButtonBindings() {
		// controller1.povUp().onTrue(new InstantCommand(() -> lift.setLevel(3)));
		// controller1.povRight().onTrue(new InstantCommand(() -> lift.setLevel(2)));
		// controller1.povLeft().onTrue(new InstantCommand(() -> lift.setLevel(1)));
		// controller1.povDown().onTrue(new InstantCommand(() -> lift.setLevel(0)));
		
		// controller1.rightBumper().onTrue(new InstantCommand(() -> lift.set(0.1)));
		// controller1.rightBumper().onFalse(new InstantCommand(() -> lift.set(0)));
		
		// controller1.leftBumper().onTrue(new InstantCommand(() -> lift.set(-0.1)));
		// controller1.leftBumper().onFalse(new InstantCommand(() -> lift.set(0)));

		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
	}

	public Command getAutonomousCommand() {
		String pathString = programChooser.getSelected();
		
		if(pathString == null){
			return Commands.none();
		}
		
		// PathPlannerTrajectory path = PathPlanner.loadPath(pathString, new PathConstraints(4.0, 3.0));
		// return new ScorePreload(lift).andThen(autoBuilder.fullAuto(path));
		return Commands.none();
	}

	private void setupAutoTab() {
		autoTab = Shuffleboard.getTab("Auto");
		programChooser = new SendableChooser<>();
		programChooser.setDefaultOption("Wait", null);
		programChooser.addOption("Score & Prepare", "ScorePreloadAndPrepare");
		programChooser.addOption("Score & Engage", "ScorePreloadAndEngage");
		programChooser.addOption("Score & Engage Station Side", "ScorePreloadAndEngageStationSide");
		programChooser.addOption("Score & Engage Wall Side", "ScorePreloadAndEngageWallSide");
		programChooser.addOption("Score & Exit", "ScorePreloadAndExit");
		autoTab.add("Auto Program", programChooser);
	}

}
