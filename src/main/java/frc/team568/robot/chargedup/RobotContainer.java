package frc.team568.robot.chargedup;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

final class RobotContainer {
	final SwerveSubsystem drive;

	public RobotContainer() {
		drive = new SwerveSubsystem();
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));
	}

	public Command getAutonomousCommand() {
		return Commands.none();
	}

}