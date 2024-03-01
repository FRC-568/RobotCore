package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.crescendo.subsystem.PneumaticSubsystem;
import frc.team568.robot.crescendo.subsystem.VisionSubsystem;

public final class RobotContainer {
	 final SwerveSubsystem drive;
	  PivotSubsystem pivot /* = new PivotSubsystem(0, 0)*/;
	   final JukeboxSubsystem jukebox = new JukeboxSubsystem(12, 11, 13);
	    final VisionSubsystem vision = new VisionSubsystem();

	Map<String, Command> eventMap = new HashMap<>();

	// Auto tab objects
	public AutoTab autoTab;
	
	// Driver tab objects
	public DriverTab driverTab;
	
	// Config tab objects
	public ConfigTab configTab;

	public FlywheelTab flywheelTab;

	// Pneumatic Subsystem object
	public PneumaticSubsystem pneumaticsub;

	ComplexWidget enterButton;

	PowerDistribution pd;


	public RobotContainer() {
		//camera = CameraServer.startAutomaticCapture();

		drive = new SwerveSubsystem(new Pose2d());
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));
		drive.configurePathplanner();

		//pivot.setDefaultCommand(new PivotSubsystemDefaultCommand(pivot));

		vision.addPoseListener(est -> drive.addVisionMeasurement(est.estimatedPose.toPose2d(), est.timestampSeconds));
		vision.startPoseListenerThread();

		jukebox.initDefaultCommand(OI.Axis.intakeSpeed, OI.Axis.outtakeSpeedL, OI.Axis.outtakeSpeedR);

		pd = new PowerDistribution(1, ModuleType.kRev);

		//autoTab = new AutoTab(this);
		driverTab = new DriverTab(this);
		configTab = new ConfigTab(this);
		flywheelTab = new FlywheelTab(this);
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

	public Command getAutonomousCommand() {
		return autoTab.chooser.getSelected();
	}
}
