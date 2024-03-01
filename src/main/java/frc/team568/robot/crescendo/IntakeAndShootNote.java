package frc.team568.robot.crescendo;

import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class IntakeAndShootNote {
	JukeboxSubsystem intakeWheels;
	JukeboxSubsystem pivot;
	JukeboxSubsystem outTakeWheels;

	public IntakeAndShootNote(JukeboxSubsystem intakeWheels, JukeboxSubsystem pivot, JukeboxSubsystem outTakeWheels) {
		this.intakeWheels = intakeWheels;
		this.pivot = pivot;
		this.outTakeWheels = outTakeWheels;
	}
}
