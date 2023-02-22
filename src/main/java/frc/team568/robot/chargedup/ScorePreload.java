package frc.team568.robot.chargedup;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class ScorePreload extends SequentialCommandGroup {
	ScorePreload(LiftSubsystem lift){
		addRequirements(lift);
		addCommands(
			new InstantCommand(() -> lift.setLevel(3)),
			new WaitUntilCommand(lift::onTarget),
			new WaitCommand(1),
			new InstantCommand(() -> lift.setLevel(1)),
			new WaitUntilCommand(lift::onTarget)
		);
	}
}
