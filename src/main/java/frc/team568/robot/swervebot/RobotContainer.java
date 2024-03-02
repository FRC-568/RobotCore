package frc.team568.robot.swervebot;

import java.util.HashMap;
import java.util.Map;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team568.robot.crescendo.subsystem.VisionSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;
import swervelib.telemetry.SwerveDriveTelemetry;

import static frc.team568.robot.swervebot.Constants.SwerveConstants.kMaxSpeed;
import static frc.team568.robot.swervebot.Constants.SwerveConstants.kWheelbaseRadius;

public final class RobotContainer {
	public final SwerveSubsystem drive;
	public final VisionSubsystem vision;

	Map<String, Command> eventMap = new HashMap<>();

	PowerDistribution pd;

	public RobotContainer() {
		drive = new SwerveSubsystem("swerve", kMaxSpeed);
		drive.initDefaultCommand(OI.Axis.swerveForward, OI.Axis.swerveLeft, OI.Axis.swerveCCW);
		SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.MACHINE;
		configurePathplanner();

		vision = new VisionSubsystem();
		vision.addPoseListener(est -> drive.addVisionMeasurement(est.estimatedPose.toPose2d(), est.timestampSeconds));
		vision.startPoseListenerThread();

		pd = new PowerDistribution(1, ModuleType.kCTRE);

		configureButtonBindings();
	}

	public void configureButtonBindings() {
		OI.Button.fieldRelativeControl.onTrue(new InstantCommand(drive::toggleFieldRelative));
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
		return null;
	}
}
