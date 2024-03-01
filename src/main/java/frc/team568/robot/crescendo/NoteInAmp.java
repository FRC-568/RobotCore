package frc.team568.robot.crescendo;

import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class NoteInAmp {
	JukeboxSubsystem pivot;
	JukeboxSubsystem outTakeWheels;

	public NoteInAmp(JukeboxSubsystem pivot, JukeboxSubsystem outTakeWheels) {
		this.pivot = pivot;
		this.outTakeWheels = outTakeWheels;
	}
}
