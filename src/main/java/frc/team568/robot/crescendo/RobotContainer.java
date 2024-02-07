package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import com.pathplanner.lib.auto.AutoBuilder;

//import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import swervelib.telemetry.SwerveDriveTelemetry;

final class RobotContainer {
	// UsbCamera camera;
	CommandXboxController controller1;
	CommandXboxController controller2;
	final SwerveSubsystem drive;
	final PivotSubsystem pivot;
	final JukeboxSubsystem jukebox;
	HashMap<String, Command> eventMap = new HashMap<>();

	// Auto tab objects
	private ShuffleboardTab autoTab;
	private ShuffleboardTab driverTab;
	private ShuffleboardTab configTab;
	private SendableChooser<String> programChooser;

	GenericEntry kpEntry;
		
	GenericEntry kiEntry;
							
	GenericEntry kdEntry;

	ComplexWidget enterButton;

	PowerDistribution pd;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);
		//camera = CameraServer.startAutomaticCapture();
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		pivot = new PivotSubsystem(0, 0);
		jukebox = new JukeboxSubsystem(1, 2, 3);

		SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));
		drive.configurePathplanner();

		configureButtonBindings();

		setupAutoTab();
		setupDriverTab();
		setupConfigTab();

		pd = new PowerDistribution(1, ModuleType.kRev);

		jukebox.setDefaultCommand(new InstantCommand(
			() -> {
				jukebox.setOuttakeSpeed(controller1.getLeftTriggerAxis(),controller1.getRightTriggerAxis());
				jukebox.setIntakeSpeed(controller1.getLeftY());
			}
		));
		
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		// controller1.leftBumper().whileTrue(new Intake(jukebox, pivot)); //intake until bumper is released
		// controller1.b().onTrue(new ScoreAmp(jukebox, pivot));
		// controller1.x().onTrue(new ScoreSpeaker(jukebox, pivot));
		// controller1.y().onTrue(new InstantCommand(() -> pivot.setAngle(0)));
		// controller1.a().onTrue(new InstantCommand(() -> pivot.setAngle(90)));
		//getRightTriggerAxis()
		// controller1.rightTrigger().whileTrue(Commands.runEnd(() -> new Command(jukebox.setOuttakeSpeed(controller1.getRightTriggerAxis(),controller1.getRightTriggerAxis())),() ->new Command(jukebox.setOuttakeSpeed(0,0))));
		// controller1.leftTrigger().whileTrue(Commands.runEnd(() -> new Command(jukebox.setIntakeSpeed(controller1.getLeftTriggerAxis())),() ->new Command(jukebox.setIntakeSpeed(0))));
	
		
	}

	public Command getAutonomousCommand() {
		// String pathString = programChooser.getSelected();
		return AutoBuilder.buildAuto("Line");
		//return Commands.none();

		// PathPlannerTrajectory path = PathPlanner.loadPath(pathString, new
		// PathConstraints(4.0, 3.0));
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
		driverTab = Shuffleboard.getTab("Driver");
		driverTab.addDouble("Travel Velocity", drive::getTravelSpeedMS).withSize(1, 1).withPosition(0, 0);
		driverTab.addDouble("Travel Direction", drive::getTravelBearingDeg).withSize(1, 1).withPosition(0, 0);
		driverTab.addDouble("Travel Facing", drive::getHeadingDeg).withSize(1, 1).withPosition(4, 0);
		// driverTab.add(camera).withPosition(0, 0).withSize(3, 3);
	}
	
	private void setupConfigTab() {
		configTab = Shuffleboard.getTab("Config");

		kpEntry = configTab.add("kp", 0)
		.withProperties(Map.of("min", 0, "max", 1))
		.withPosition(0, 0)
		.withSize(1, 1)
		.getEntry();

		
		kiEntry = configTab.add("ki",0)
		.withProperties(Map.of("min", 0, "max", 1))
		.withPosition(1, 0)
		.withSize(1, 1)
		.getEntry();
							
		kdEntry = configTab.add("kd", 0)
		.withProperties(Map.of("min", 0, "max", 1))
		.withPosition(2, 0)
		.withSize(1, 1)
		.getEntry();

		enterButton = configTab.add(new InstantCommand(() -> 
			pivot.populate(kpEntry.getDouble(0), kiEntry.getDouble(0), kdEntry.getDouble(0))
		));
		
	}
}
