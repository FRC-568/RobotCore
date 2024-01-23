package frc.team568.robot.crescendo;

import java.util.HashMap;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

//import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


final class RobotContainer {
	//UsbCamera camera;
	CommandXboxController controller1;
	CommandXboxController controller2;
	final SwerveSubsystem drive;
	HashMap<String, Command> eventMap = new HashMap<>();

	// Auto tab objects
	private ShuffleboardTab autoTab;
	private ShuffleboardTab driverTab;
	private SendableChooser<String> programChooser;

	PowerDistribution pd;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);
		//camera = CameraServer.startAutomaticCapture();
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));

		configureButtonBindings();

		// 2024 Migration - keeping code for reference; placeholders and numbers are estimated and will need to be measured on the bot.
		AutoBuilder.configureHolonomic(
				drive::getPose, // Pose2d supplier
				drive::resetPose, // Pose2d consumer, used to reset odometry at the beginning of auto
				drive::getChassisSpeeds,
				drive::setModuleStates, // SwerveDriveKinematics
				new HolonomicPathFollowerConfig(
					new PIDConstants(5.0, 0.0, 0.0), // PID constants to correct for translation error (used to create the X and Y PID controllers)
					new PIDConstants(0.5, 0.0, 0.0),
					5.0,
					0.5,
					new ReplanningConfig()), // PID constants to correct for rotation error (used to create the rotation controller)
				() -> true, // Should the path be automatically mirrored depending on alliance color. Optional, defaults to true
				drive // The drive subsystem. Used to properly set the requirements of path following commands
			);

		setupAutoTab();
		setupDriverTab();

		pd = new PowerDistribution(1, ModuleType.kRev);
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		controller1.leftBumper().toggleOnTrue(new InstantCommand(() -> drive.toggleSlowMode()));
	}

	public Command getAutonomousCommand() {
		// String pathString = programChooser.getSelected();
		

		return Commands.none();
		
		// PathPlannerTrajectory path = PathPlanner.loadPath(pathString, new PathConstraints(4.0, 3.0));
		// return new ScorePreload(lift).andThen(autoBuilder.fullAuto(path))
		
		// return Commands.none();
	}

	private void setupAutoTab() {
		autoTab = Shuffleboard.getTab("Auto");
		programChooser = new SendableChooser<>();
		programChooser.setDefaultOption("Wait", null);
		autoTab.add("Auto Program", programChooser);
	}

	private void setupDriverTab() {
		driverTab = Shuffleboard.getTab( "Driver");
		driverTab.addDouble("Travel Velocity", drive::getTravelSpeedMS).withSize(1, 1).withPosition(0, 0);
		driverTab.addDouble("Travel Direction", drive::getTravelBearingDeg).withSize(1, 1).withPosition(0, 0);
		driverTab.addDouble("Travel Facing", drive::getHeadingDeg).withSize(1, 1).withPosition(4, 0);
		//driverTab.add(camera).withPosition(0, 0).withSize(3, 3);
	}

}
