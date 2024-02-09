package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerTrajectory;

//import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.team568.robot.crescendo.command.Aim;
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
import frc.team568.robot.crescendo.command.Closing;
import frc.team568.robot.crescendo.command.DownPneumatic;
import frc.team568.robot.crescendo.command.Intake;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Up;
import frc.team568.robot.crescendo.command.UpPneumatic;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import swervelib.telemetry.SwerveDriveTelemetry;

public final class RobotContainer {
	// UsbCamera camera;
	CommandXboxController controller1;
	CommandXboxController controller2;
	final SwerveSubsystem drive;
	final PivotSubsystem pivot = new PivotSubsystem(0, 0);
	final JukeboxSubsystem jukebox = new JukeboxSubsystem(0, 0, 0);
	HashMap<String, Command> eventMap = new HashMap<>();


	// Auto tab objects
	private ShuffleboardTab autoTab;
	private ShuffleboardTab driverTab;
	private ShuffleboardTab configTab;
	private SendableChooser<Command> programChooser;

	private DoubleSolenoid dSolenoid = new DoubleSolenoid(null, 0, 0);


	GenericEntry kpEntry;
		
	GenericEntry kiEntry;
							
	GenericEntry kdEntry;

	ComplexWidget enterButton;

	PowerDistribution pd;

	private Command aim;
	private Command autoScoreAndPreload;
	private Command closing;
	private Command downpneumatic;
	private Command intake;
	private Command scoreamp;
	private Command scorespeaker;
	private Command up;
	private Command uppneumatic;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);
		//camera = CameraServer.startAutomaticCapture();
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		// pivot = new PivotSubsystem(0, 0);
		// jukebox = new JukeboxSubsystem(1, 2, 3);

		SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));
		drive.configurePathplanner();

		configureButtonBindings();

		setupAutoTab();
		setupDriverTab();
		setupConfigTab();

		pd = new PowerDistribution(1, ModuleType.kRev);
/* 
		jukebox.setDefaultCommand(new InstantCommand(
			() -> {
				jukebox.setOuttakeSpeed(controller1.getLeftTriggerAxis(),controller1.getRightTriggerAxis());
				jukebox.setIntakeSpeed(controller1.getLeftY());
			}
		));
		*/

		aim = new Aim();
		autoScoreAndPreload = new AutoScoreAndPreload(drive, jukebox, pivot);
		closing = new Closing(pivot);
		downpneumatic = new DownPneumatic(dSolenoid);
		intake = new Intake(jukebox, pivot);
		scoreamp = new ScoreAmp(jukebox, pivot);
		scorespeaker = new ScoreSpeaker(jukebox, pivot);
		up = new Up(pivot);
		uppneumatic = new UpPneumatic(dSolenoid);
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		// OI.Button.intake.whileTrue(new Intake(jukebox, pivot)); //intake until bumper is released
		// OI.Button.scoreAmp.onTrue(new ScoreAmp(jukebox, pivot));
		// OI.Button.scoreSpeaker.onTrue(new ScoreSpeaker(jukebox, pivot));
		// OI.Button.pivotDown.onTrue(new InstantCommand(() -> pivot.setAngle(0)));
		// OI.Button.pivotUp.onTrue(new InstantCommand(() -> pivot.setAngle(90)));
		//getRightTriggerAxis()
		// OI.Button.runOuttake.whileTrue(Commands.runEnd(() -> new Command(jukebox.setOuttakeSpeed(OI.Axis.outtakeSpeed.getAsDouble(), OI.Axis.outtakeSpeed.getAsDouble())), () -> new Command(jukebox.setOuttakeSpeed(0,0))));
		// OI.Button.runIntake.whileTrue(Commands.runEnd(() -> new Command(jukebox.setIntakeSpeed(OI.Axis.intakeSpeed.getAsDouble())), () -> new Command(jukebox.setIntakeSpeed(0))));
		controller1.back().onTrue(AutoBuilder.buildAuto("Backwards Line"));
		
	}

	public Command getAutonomousCommand() {
		return programChooser.getSelected();
	}

	private void setupAutoTab() {
		programChooser = new SendableChooser<>();

		programChooser.setDefaultOption("Wait", null);
		OI.autoTab.add("Auto Program", programChooser);

		programChooser.addOption("Aim", aim);
		programChooser.addOption("AutoScoreAndPreload", autoScoreAndPreload);
		programChooser.addOption("Closing", closing);
		programChooser.addOption("Closing", downpneumatic);
		programChooser.addOption("Intake", intake);
		programChooser.addOption("Score AMP", scoreamp);
		programChooser.addOption("Score Speaker", scorespeaker);
		programChooser.addOption("Up", up);
		programChooser.addOption("Up Pneumatic", uppneumatic);

		SmartDashboard.putData(programChooser);
		autoTab.add("Auto Program", programChooser);

		
	}

	private void setupDriverTab() {
		OI.driverTab.addDouble("Travel Velocity", drive::getTravelSpeedMS).withSize(1, 1).withPosition(0, 0);
		OI.driverTab.addDouble("Travel Direction", drive::getTravelBearingDeg).withSize(1, 1).withPosition(0, 0);
		OI.driverTab.addDouble("Travel Facing", drive::getHeadingDeg).withSize(1, 1).withPosition(4, 0);
		// driverTab.add(camera).withPosition(0, 0).withSize(3, 3);
	}
	
	private void setupConfigTab() {
		kpEntry = OI.configTab.add("kp", 0)
		.withProperties(Map.of("min", 0, "max", 1))
		.withPosition(0, 0)
		.withSize(1, 1)
		.getEntry();
		
		kiEntry = OI.configTab.add("ki",0)
		.withProperties(Map.of("min", 0, "max", 1))
		.withPosition(1, 0)
		.withSize(1, 1)
		.getEntry();
							
		kdEntry = OI.configTab.add("kd", 0)
		.withProperties(Map.of("min", 0, "max", 1))
		.withPosition(2, 0)
		.withSize(1, 1)
		.getEntry();
		
		// EnterButton = configTab.add(new InstantCommand(() -> 
		// pivot.populate(kpEntry.getDouble(0), kiEntry.getDouble(0), kdEntry.getDouble(0))
		// ));
	}
}
