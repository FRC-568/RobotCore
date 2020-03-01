package frc.team568.robot.recharge;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

import frc.team568.robot.recharge.Constants.AutoConstants;
import frc.team568.robot.recharge.Constants.DriveConstants;

public class RobotContainer {
	final DriveTrain2020 robotDrive = new DriveTrain2020();

	public RobotContainer() {

	}


public Command getAtonomousCommand() {
	 
	
	//get trajectory file
	String trajectoryJSON = "paths/YourPath.wpilib.json";
	try {
  		Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
  		Trajectory exampleTrajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
	} catch (IOException ex) {
  		DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
	}
	// Create a voltage constraint to ensure we don't accelerate too fast
	 var autoVoltageConstraint =
	 new DifferentialDriveVoltageConstraint(
		 new SimpleMotorFeedforward(DriveConstants.ksVolts,
									DriveConstants.kvVoltSecondsPerMeter,
									DriveConstants.kaVoltSecondsSquaredPerMeter),
		 DriveConstants.kDriveKinematics,
		 10);

	// Create config for trajectory
    TrajectoryConfig config =
        new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
                             AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DriveConstants.kDriveKinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

	// An example trajectory to follow.  All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(
            new Translation2d(1, 1),
            new Translation2d(2, -1)
        ),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),
        // Pass config
        config
    );

			RamseteCommand ramseteCommand = new RamseteCommand(
				exampleTrajectory,
				robotDrive::getPose,
				new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
				new SimpleMotorFeedforward(DriveConstants.ksVolts,
										   DriveConstants.kvVoltSecondsPerMeter,
										   DriveConstants.kaVoltSecondsSquaredPerMeter),
				DriveConstants.kDriveKinematics,
				robotDrive::getWheelSpeeds,
				new PIDController(DriveConstants.kPDriveVel, 0, 0),
				new PIDController(DriveConstants.kPDriveVel, 0, 0),
				// RamseteCommand passes volts to the callback
				robotDrive::tankDriveVolts,
				robotDrive
			);
		
			// Run path following command, then stop at the end.
			return ramseteCommand.andThen(() -> robotDrive.tankDriveVolts(0, 0));
	}	
}