package frc.team568.robot.chargedup;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.team568.robot.subsystems.AprilTags;

final class RobotContainer {
	final SwerveSubsystem drive;
	final AprilTags tracker;

	public RobotContainer() {
		drive = new SwerveSubsystem();
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));

		tracker = new AprilTags("photonvision", new Translation3d(0.0, 0.0, 0.0), new Rotation3d(0.0, 0.0, 0.0));
	}

	public Command getAutonomousCommand() {
		return Commands.none();
	}

}