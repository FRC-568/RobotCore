package frc.team568.robot.crescendo;

import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class ShootNote {
	JukeboxSubsystem pivot;
	JukeboxSubsystem outTakeWheel;

	public ShootNote(JukeboxSubsystem pivot, JukeboxSubsystem outTakeWheel) {
		this.pivot = pivot;
		this.outTakeWheel = outTakeWheel;
	}
}
