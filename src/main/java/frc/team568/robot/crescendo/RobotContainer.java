package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.crescendo.subsystem.PneumaticSubsystem;
import frc.team568.robot.crescendo.subsystem.VisionSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;

import static frc.team568.robot.crescendo.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.crescendo.Constants.SwerveConstants.kWheelbaseRadius;

public final class RobotContainer {
	public final SwerveSubsystem drive;
	public PivotSubsystem pivot /* = new PivotSubsystem(0, 0)*/;
	public final JukeboxSubsystem jukebox = new JukeboxSubsystem(12, 11, 13);
	public final VisionSubsystem vision = new VisionSubsystem();

	Map<String, Command> eventMap = new HashMap<>();

	// Auto tab objects
	public AutoTab autoTab;
	
	// Driver tab objects
	public DriverTab driverTab;
	
	// Config tab objects
	public ConfigTab configTab;

	//public FlywheelTab flywheelTab;

	// Pneumatic Subsystem object
	public PneumaticSubsystem pneumaticsub;

	ComplexWidget enterButton;

	PowerDistribution pd;


	public RobotContainer() {
		drive = new SwerveSubsystem("swerve", kMaxSpeed);
		drive.initDefaultCommand(OI.Axis.swerveForward, OI.Axis.swerveLeft, OI.Axis.swerveCCW);
		configurePathplanner();

		//pivot.setDefaultCommand(new PivotSubsystemDefaultCommand(pivot));

		vision.addPoseListener(est -> drive.addVisionMeasurement(est.estimatedPose.toPose2d(), est.timestampSeconds));
		vision.startPoseListenerThread();

		jukebox.initDefaultCommand(OI.Axis.intakeSpeed, OI.Axis.outtakeSpeedL, OI.Axis.outtakeSpeedR);

		pd = new PowerDistribution(1, ModuleType.kRev);

		//autoTab = new AutoTab(this);
		driverTab = new DriverTab(this);
		configTab = new ConfigTab(this);
		//flywheelTab = new FlywheelTab(this);
		pneumaticsub = new PneumaticSubsystem();

		configureButtonBindings();
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
		// OI.Button.intake.whileTrue(new Intake(jukebox, pivot)); //intake until bumper is released
		// OI.Button.scoreAmp.onTrue(new ScoreAmp(jukebox, pivot));
		// OI.Button.scoreSpeaker.onTrue(new ScoreSpeaker(jukebox, pivot));
		// OI.Button.pivotDown.onTrue(new InstantCommand(() -> pivot.setAngle(0)));
		// OI.Button.pivotUp.onTrue(new InstantCommand(() -> pivot.setAngle(90)));
		OI.Button.pneumaticstateswitch.onTrue(new InstantCommand(pneumaticsub::SwitchState));
		OI.driverController.back().onTrue(AutoBuilder.buildAuto("Backwards Line"));
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
					1.0,
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

	public Command getAutonomousCommand() {
		return autoTab.chooser.getSelected();
	}
}
