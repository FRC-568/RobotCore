package frc.team568.robot.chargedup;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class DriveForwardIGuess extends SequentialCommandGroup {
	DriveForwardIGuess(SwerveSubsystem drive) {
		addRequirements(drive);
		addCommands(
			new InstantCommand(() -> drive.drive(0, 0.5, 0)),
			new WaitCommand(5),
			new InstantCommand(() -> drive.drive(0, 0, 0))
		);
	}
}
