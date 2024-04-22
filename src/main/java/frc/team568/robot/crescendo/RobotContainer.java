package frc.team568.robot.crescendo;

import static frc.team568.robot.crescendo.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.crescendo.Constants.SwerveConstants.kWheelbaseRadius;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
<<<<<<< HEAD
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
=======
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.team568.robot.crescendo.command.Aim;
import frc.team568.robot.crescendo.command.CommandFactory;
import frc.team568.robot.crescendo.command.GoToSpeaker;
import frc.team568.robot.crescendo.command.HomePivot;
import frc.team568.robot.crescendo.command.Intake;
import frc.team568.robot.crescendo.command.LookAtSpeaker;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Shoot;
>>>>>>> 353edeb5001dc042ab15a96481056c8a02ea94b0
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.crescendo.subsystem.PneumaticSubsystem;
import frc.team568.robot.crescendo.subsystem.VisionSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;

<<<<<<< HEAD
final class RobotContainer {
	// UsbCamera camera;
	CommandXboxController controller1;
	CommandXboxController controller2;
	final SwerveSubsystem drive;
	final PivotSubsystem pivot;
	final JukeboxSubsystem jukebox;
	HashMap<String, Command> eventMap = new HashMap<>();
=======
public final class RobotContainer {
	private Notifier rearCameraStarter;
>>>>>>> 353edeb5001dc042ab15a96481056c8a02ea94b0

	public final SwerveSubsystem drive;
	public PivotSubsystem pivot;
	public final JukeboxSubsystem jukebox;
	public VisionSubsystem targetVision;
	public VisionSubsystem rearVision;
	public final PneumaticSubsystem lift;
	public final PowerDistribution pd;

	public AutoTab autoTab;
	public DriverTab driverTab;
	public ConfigTab configTab;
	public FlywheelTab flywheelTab;

	public RobotContainer() {
<<<<<<< HEAD
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);
		//camera = CameraServer.startAutomaticCapture();
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		jukebox = new JukeboxSubsystem(0, 0, 0);
		pivot = new PivotSubsystem(0, 0);
=======
		drive = new SwerveSubsystem("crescendo", kMaxSpeed);
		drive.initDefaultCommand(OI.Axis.swerveForward, OI.Axis.swerveLeft, OI.Axis.swerveCCW);
		
		configurePathplanner();
>>>>>>> 353edeb5001dc042ab15a96481056c8a02ea94b0

		pivot = new PivotSubsystem();
		pivot.initDefaultCommand(OI.Axis.pivotPower);

		jukebox = new JukeboxSubsystem();
		jukebox.setOuttakeBias(-0.0);
		jukebox.initDefaultCommand(OI.Axis.intakeSpeed, OI.Axis.outtakeSpeed);

		// setupCameras();
		
		lift = new PneumaticSubsystem();
		lift.setInterupter(() -> jukebox.getLeftVelo() != 0);

		pd = new PowerDistribution(1, ModuleType.kRev);

		registerPathPlannerCommands();

		autoTab = new AutoTab(this);
		driverTab = new DriverTab(this);
		// configTab = new ConfigTab(this);
		flywheelTab = new FlywheelTab(this);

		configureButtonBindings();
	}

	public void registerPathPlannerCommands() {
		var factory = new CommandFactory(drive, pivot);

		NamedCommands.registerCommand("Aim", new Aim(pivot));
		NamedCommands.registerCommand("AimAtSpeaker", factory.aimAtSpeaker().withTimeout(1));
		NamedCommands.registerCommand("Intake", new Intake(jukebox, pivot));
		NamedCommands.registerCommand("HomePivot", new HomePivot(pivot));
		NamedCommands.registerCommand("LaunchNote", jukebox.getLaunchNoteCommand());
		NamedCommands.registerCommand("ScoreSpeaker", new ScoreSpeaker(jukebox, pivot, drive::getPose, false));
		NamedCommands.registerCommand("ScoreAmp", new ScoreAmp(jukebox, pivot));
		NamedCommands.registerCommand("Shoot", new Shoot(jukebox));
		NamedCommands.registerCommand("UpPneumatic", lift.getExtendCommand());
		NamedCommands.registerCommand("DownPneumatic", lift.getRetractCommand());
		NamedCommands.registerCommand("GoToNoteOne", factory.goToNoteOne());
		NamedCommands.registerCommand("GoToSpeaker", new GoToSpeaker(drive));
		NamedCommands.registerCommand("LookAtSpeaker", new LookAtSpeaker(drive));
		NamedCommands.registerCommand("WaitUntilIntake", Commands.waitUntil(jukebox::hasNote));
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		// OI.Button.slowmodeControl.onTrue(new InstantCommand(drive::toggleSlowMode));
		OI.Button.resetHeading.onTrue(new InstantCommand(() -> drive.resetPose(new Pose2d(drive.getPose().getTranslation(), new Rotation2d(0)))));
		OI.Button.compressorOff.onTrue(new InstantCommand(() -> lift.disableCompressor()));
		OI.Button.compressorOn.onTrue(new InstantCommand(() -> lift.enableCompressor()));
		// OI.Button.scoreAmp.onTrue(new ScoreAmp(jukebox, pivot));
		// OI.Button.scoreSpeaker.onTrue(new ScoreSpeaker(jukebox, pivot));
		OI.Button.pivotHalf.onTrue(
			new RunCommand(() -> pivot.setPower(0.2), pivot).withTimeout(0.2)
			.andThen(new RunCommand(() -> pivot.moveToward(15), pivot)));
		OI.Button.pivotUp.onTrue(
			new RunCommand(() -> pivot.setPower(0.2), pivot).withTimeout(0.2)
			.andThen(new RunCommand(() -> pivot.moveToward(90), pivot)));
		OI.Button.pivotDown.onTrue(new RunCommand(() -> pivot.moveToward(0), pivot));
	
		//new RunCommand(() -> pivot.setPower(OI.Axis.pivotPower.getAsDouble()), pivot).schedule();

		OI.Button.pneumaticstateswitch.onTrue(lift.getToggleCommand());
		OI.Button.shoot.onTrue(new Shoot(jukebox));
		// OI.Button.softShoot.onTrue(new SoftShoot(jukebox));
		OI.Button.shootAmp.onTrue(new RunCommand(() -> jukebox.runIntake(0.3), jukebox).withTimeout(1.5)
									.raceWith(new RunCommand(() -> jukebox.runOuttake(5)).withTimeout(1.5)));
		OI.Button.intake.whileTrue(new Intake(jukebox));

		OI.Button.userButton.onTrue(new InstantCommand(pivot::toggleBrakes));
	}

	public void configurePathplanner() {
		AutoBuilder.configureHolonomic(
				drive::getPose, // Pose2d supplier
				drive::resetPose, // Pose2d consumer, used to reset odometry at the beginning of auto
				drive::getChassisSpeeds,
				drive::setModuleStates, // SwerveDriveKinematics
				new HolonomicPathFollowerConfig(
					new PIDConstants(0.03, 0.0, 0.05), // PID constants to correct for translation error (used to create the X and Y PID controllers)
					new PIDConstants(0.003, 0.0, 0),
					4.5,
					kWheelbaseRadius,
					new ReplanningConfig(true, true, 0.09, 0.3)), // PID constants to correct for rotation error (used to create the rotation controller)
				() -> {
					var alliance = DriverStation.getAlliance();
					return alliance.isPresent()
						? alliance.get() == DriverStation.Alliance.Red
						: false;
				},
				drive
			);
	}

	public void setupCameras() {
		try {
			targetVision = new VisionSubsystem("target");
			targetVision.addPoseListener((est, dev) -> drive.addVisionMeasurement(est.estimatedPose.toPose2d(), est.timestampSeconds, dev));
			targetVision.startPoseListenerThread();
		} catch (Exception e) {
			targetVision = null;
			DriverStation.reportWarning("Could not initialize target cam.", e.getStackTrace());
		}

		try {
			rearVision = new VisionSubsystem("rear");
			rearVision.addPoseListener((est, dev) -> drive.addVisionMeasurement(est.estimatedPose.toPose2d(), est.timestampSeconds, dev));
			rearCameraStarter = new Notifier(rearVision::startPoseListenerThread);
			rearCameraStarter.startSingle(0.5);
		} catch (Exception e) {
			rearVision = null;
			DriverStation.reportWarning("Could not initialize rear cam.", e.getStackTrace());
		}
	}

	public Command getAutonomousCommand() {
<<<<<<< HEAD
		String pathString = programChooser.getSelected();
		if(pathString.equals("Line")){
			return AutoBuilder.buildAuto("Line");
		}
		else if( pathString.equals("score") ){
			return new AutoScoreAndPreload(drive, jukebox, pivot);
		}
		
		return Commands.none();

		// PathPlannerTrajectory path = PathPlanner.loadPath(pathString, new
		// PathConstraints(4.0, 3.0));
		// return new ScorePreload(lift).andThen(autoBuilder.fullAuto(path))

		// return Commands.none();
	}

	private void setupAutoTab() {
		autoTab = Shuffleboard.getTab("Auto");
		programChooser = new SendableChooser<>();
		programChooser.setDefaultOption("Wait", null);
		programChooser.addOption("auto score and preload", "score");

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
		
		// EnterButton = configTab.add(new InstantCommand(() -> 
		// pivot.populate(kpEntry.getDouble(0), kiEntry.getDouble(0), kdEntry.getDouble(0))
		// ));
=======
		// if (autoTab.getAutonomousCommand() == null) {
		// 	return Commands.none();
		// } else {
			return autoTab.getAutonomousCommand();
		// }
>>>>>>> 353edeb5001dc042ab15a96481056c8a02ea94b0
	}
}
