package frc.team568.robot.chargedup;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class DriveForwardIGuess extends SequentialCommandGroup {
	DriveForwardIGuess(SwerveSubsystem drive, double runtime) {
		addRequirements(drive);
		addCommands(
			new InstantCommand(() -> drive.drive(0, 1, 0)),
			new WaitCommand(runtime),
			new InstantCommand(() -> drive.drive(0, 0, 0))
		);
	}
}
