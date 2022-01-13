package frc.team568.robot.recharge;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.team568.robot.recharge.Constants.AutoConstants;
import frc.team568.robot.recharge.Constants.DriveConstants;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class RobotContainer {
	TalonSRXDrive drive; 
	public RobotContainer(TalonSRXDrive drive) {
		this.drive = drive;
	}

	public Command getAutonomousCommand() {
		
		final String trajectoryJSON = "PathWeaver/pathweaver.json";
		Trajectory trajectory = null;
		try {
			final Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
			trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
		} catch (final IOException ex) {
			DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
		}

		final RamseteCommand ramseteCommand = new RamseteCommand(
			trajectory,
			drive::getPose,
			new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
			new SimpleMotorFeedforward(DriveConstants.ksVolts,
										DriveConstants.kvVoltSecondsPerMeter,
										DriveConstants.kaVoltSecondsSquaredPerMeter),
			DriveConstants.kDriveKinematics,
			drive::getWheelSpeeds,
			new PIDController(DriveConstants.kPDriveVel, 0, 0),
			new PIDController(DriveConstants.kPDriveVel, 0, 0),
			// RamseteCommand passes volts to the callback
			drive::tankDriveVolts,
			drive
		);
		
		// Run path following command, then stop at the end.
		return ramseteCommand.andThen(() -> drive.tankDriveVolts(0, 0));
	}	
	
}