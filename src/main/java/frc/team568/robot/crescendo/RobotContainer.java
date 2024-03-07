package frc.team568.robot.crescendo;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import java.util.Optional;

<<<<<<< HEAD
import frc.team568.robot.crescendo.command.Aim;
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
import frc.team568.robot.crescendo.command.Closing;
import frc.team568.robot.crescendo.command.HomePivot;
import frc.team568.robot.crescendo.command.Intake;
import frc.team568.robot.crescendo.command.NoteRun;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Shoot;
import frc.team568.robot.crescendo.command.Up;
=======
import frc.team568.robot.crescendo.command.Shoot;
>>>>>>> a1b0507 (add one button outtake)
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.crescendo.subsystem.PneumaticSubsystem;
import frc.team568.robot.crescendo.subsystem.VisionSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;

import static frc.team568.robot.crescendo.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.crescendo.Constants.SwerveConstants.kWheelbaseRadius;

public final class RobotContainer {
	public Optional<Alliance> alliance;

	public final SwerveSubsystem drive;
<<<<<<< HEAD
	public PivotSubsystem pivot;
	public final JukeboxSubsystem jukebox;
	public final VisionSubsystem vision;
	public final PneumaticSubsystem lift;
	public final PowerDistribution pd;
=======
	public PivotSubsystem pivot /* = new PivotSubsystem(0, 0)*/;
	public final JukeboxSubsystem jukebox = new JukeboxSubsystem(11, 12, 13);
	public final VisionSubsystem vision = new VisionSubsystem();
>>>>>>> a1b0507 (add one button outtake)

	public AutoTab autoTab;
	public DriverTab driverTab;
	public ConfigTab configTab;
	//public FlywheelTab flywheelTab;

	public RobotContainer() {
		alliance = DriverStation.getAlliance();

		drive = new SwerveSubsystem("crescendo", kMaxSpeed);
		drive.initDefaultCommand(OI.Axis.swerveForward, OI.Axis.swerveLeft, OI.Axis.swerveCCW);
		configurePathplanner();

		//pivot = new PivotSubsystem(0, 0);
		//pivot.setDefaultCommand(new PivotSubsystemDefaultCommand(pivot));

		jukebox = new JukeboxSubsystem();
		jukebox.initDefaultCommand(OI.Axis.intakeSpeed, OI.Axis.outtakeSpeedL, OI.Axis.outtakeSpeedR);

		vision = new VisionSubsystem();
		vision.addPoseListener(est -> drive.addVisionMeasurement(est.estimatedPose.toPose2d(), est.timestampSeconds));
		vision.startPoseListenerThread();

		lift = new PneumaticSubsystem();

		pd = new PowerDistribution(1, ModuleType.kRev);

		registerPathPlannerCommands();

		//autoTab = new AutoTab(this);
		driverTab = new DriverTab(this);
		configTab = new ConfigTab(this);
		//flywheelTab = new FlywheelTab(this);

		configureButtonBindings();
	}

	public void registerPathPlannerCommands(){
		NamedCommands.registerCommand("Aim", new Aim(pivot, null));
		NamedCommands.registerCommand("AutoScoreAndPreload", new AutoScoreAndPreload(drive, jukebox, pivot));
		NamedCommands.registerCommand("Intake", new Intake(jukebox, pivot));
		NamedCommands.registerCommand("Closing", new Closing(pivot));
		NamedCommands.registerCommand("HomePivot", new HomePivot(pivot));
		NamedCommands.registerCommand("NoteRun", new NoteRun(jukebox));
		NamedCommands.registerCommand("ScoreSpeaker", new ScoreSpeaker(jukebox, pivot));
		NamedCommands.registerCommand("ScoreAmp", new ScoreAmp(jukebox, pivot));
		NamedCommands.registerCommand("Shoot", new Shoot());
		NamedCommands.registerCommand("Up", new Up(pivot));
		NamedCommands.registerCommand("UpPneumatic", lift.getExtendCommand());
		NamedCommands.registerCommand("DownPneumatic", lift.getRetractCommand());
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		// OI.Button.intake.whileTrue(new Intake(jukebox, pivot)); //intake until bumper is released
		// OI.Button.scoreAmp.onTrue(new ScoreAmp(jukebox, pivot));
		// OI.Button.scoreSpeaker.onTrue(new ScoreSpeaker(jukebox, pivot));
		// OI.Button.pivotDown.onTrue(new InstantCommand(() -> pivot.setAngle(0)));
		// OI.Button.pivotUp.onTrue(new InstantCommand(() -> pivot.setAngle(90)));
<<<<<<< HEAD
		OI.Button.pneumaticstateswitch.onTrue(lift.getToggleCommand());
=======
		OI.Button.pneumaticstateswitch.onTrue(new InstantCommand(pneumaticsub::SwitchState));
		OI.driverController.back().onTrue(AutoBuilder.buildAuto("Backwards Line"));
		OI.Button.shoot.onTrue(new Shoot(jukebox));
>>>>>>> a1b0507 (add one button outtake)
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
					// var alliance = DriverStation.getAlliance();
					return alliance.isPresent()
						? alliance.get() == DriverStation.Alliance.Red
						: false;
				},
				drive
			);
	}

	public Command getAutonomousCommand() {
		return autoTab.chooser.getSelected();
	}
}
