package frc.team568.robot.chargedup;

import java.util.HashMap;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
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
	private static final Translation2d ZERO_POS = new Translation2d(0, 0);

	UsbCamera camera;
	CommandXboxController controller1;
	CommandXboxController controller2;
	final SwerveSubsystem drive;
	final LiftSubsystem lift;
	HashMap<String, Command> eventMap = new HashMap<>();
	LockDemWheels lockWheels;

	// Auto tab objects
	private ShuffleboardTab autoTab;
	private ShuffleboardTab driverTab;
	private SendableChooser<String> programChooser;

	PowerDistribution pd;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);
		camera = CameraServer.startAutomaticCapture();
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));
		lockWheels = new LockDemWheels(drive);

		lift = new LiftSubsystem(12, 11, 0, 1);

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
		controller1.povUp().onTrue(new InstantCommand(() -> lift.setLevel(4)));
		controller1.povRight().onTrue(new InstantCommand(() -> lift.setLevel(3)));
		controller1.povLeft().onTrue(new InstantCommand(() -> lift.setLevel(2)));
		controller1.povDown().onTrue(new InstantCommand(() -> lift.setLevel(0)));

		controller1.a().onTrue(new InstantCommand(() -> lift.setLevel(1)));
		controller1.rightBumper().onTrue(new InstantCommand(() -> lift.setLevel(5)));

		controller1.back().onTrue(lockWheels);

		controller2.rightTrigger().whileTrue(Commands.runEnd(() -> lift.setCarriage(controller2.getRightTriggerAxis()), () -> lift.setCarriage(0), lift));
		controller2.leftTrigger().whileTrue(Commands.runEnd(() -> lift.setCarriage(-controller2.getLeftTriggerAxis()), () -> lift.setCarriage(0), lift));
	
		// TO-DO: check parens
		// controller2.leftStick().whileTrue(Commands.runEnd(() -> lift.setStage(MathUtil.applyDeadband(controller2.getLeftY(), kControllerDeadband)), () -> lift.setStage(0), lift));
		// controller2.rightStick().whileTrue(Commands.runEnd(() -> lift.setCarriage(MathUtil.applyDeadband(controller2.getRightY(), kControllerDeadband)), () -> lift.setCarriage(0), lift));
		
		controller2.povUp().onTrue(new InstantCommand(() -> lift.setStage(1)));
		controller2.povUp().onFalse(new InstantCommand(() -> lift.setStage(0)));
		
		controller2.povDown().onTrue(new InstantCommand(() -> lift.setStage(-1)));
		controller2.povDown().onFalse(new InstantCommand(() -> lift.setStage((0))));

		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		controller1.leftBumper().toggleOnTrue(new InstantCommand(() -> drive.toggleSlowMode()));
	}

	public Command getAutonomousCommand() {
		String pathString = programChooser.getSelected();
		
		if (pathString == null) {
			return Commands.none();
		} else if (pathString == "short") {
			return new DriveDistanceIGuess(drive, 2, 0, 0.5, 0);
		} else if (pathString == "long") {
			return new DriveDistanceIGuess(drive, 3, 0, 0.5, 0);
		} else if (pathString == "test") {
			return new DriveDistanceIGuess(drive, 0.5, 0, 0.25, 0);
		}

		return Commands.none();
		
		// PathPlannerTrajectory path = PathPlanner.loadPath(pathString, new PathConstraints(4.0, 3.0));
		// return new ScorePreload(lift).andThen(autoBuilder.fullAuto(path))
		
		// return Commands.none();
	}

	private void setupAutoTab() {
		autoTab = Shuffleboard.getTab("Auto");
		programChooser = new SendableChooser<>();
		programChooser.setDefaultOption("Wait", null);
		programChooser.addOption("Short boi", "short");
		programChooser.addOption("Long boi", "long");
		programChooser.addOption("Test boi", "test");
		// programChooser.addOption("Score & Prepare", "ScorePreloadAndPrepare");
		// programChooser.addOption("Score & Engage", "ScorePreloadAndEngage");
		// programChooser.addOption("Score & Engage Station Side", "ScorePreloadAndEngageStationSide");
		// programChooser.addOption("Score & Engage Wall Side", "ScorePreloadAndEngageWallSide");
		// programChooser.addOption("Score & Exit", "ScorePreloadAndExit");
		autoTab.add("Auto Program", programChooser);
	}

	private void setupDriverTab() {
		driverTab = Shuffleboard.getTab( "Driver");
		driverTab.addDouble("Travel Velocity", () -> drive.getTargetTrajectory().getDistance(ZERO_POS)).withSize(1, 1).withPosition(0, 0);
		driverTab.addDouble("Travel Direction", () -> drive.getTargetTrajectory().getAngle().getDegrees()).withSize(1, 1).withPosition(0, 0);
		driverTab.addDouble("Travel Facing", () -> drive.getTargetRotation().getDegrees()).withSize(1, 1).withPosition(4, 0);
		driverTab.add(camera).withPosition(0, 0).withSize(3, 3);
	}

}
