package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import com.pathplanner.lib.auto.AutoBuilder;
//import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
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
	public AutoTab autoTab;
	
	// Driver tab objects
	public DriverTab driverTab;
	
	// Config tab objects
	public ConfigTab configTab;

	public DoubleSolenoid dSolenoid = new DoubleSolenoid(null, 0, 0);


	ComplexWidget enterButton;

	PowerDistribution pd;


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
		drive.setDefaultCommand(new PivotSubsystemDefaultCommand(pivot));
		drive.configurePathplanner();

		configureButtonBindings();

		autoTab = new AutoTab(this);
		driverTab = new DriverTab(this);
		configTab = new ConfigTab(this);

		pd = new PowerDistribution(1, ModuleType.kRev);
/* 
		jukebox.setDefaultCommand(new InstantCommand(
			() -> {
				jukebox.setOuttakeSpeed(controller1.getLeftTriggerAxis(),controller1.getRightTriggerAxis());
				jukebox.setIntakeSpeed(controller1.getLeftY());
			}
		));
		*/
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
		return autoTab.chooser.getSelected();
	}
}
