package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class NoteInAmp extends Command{
	JukeboxSubsystem pivot;
	JukeboxSubsystem outTakeWheels;

	public NoteInAmp(JukeboxSubsystem pivot, JukeboxSubsystem outTakeWheels) {
		this.pivot = pivot;
		this.outTakeWheels = outTakeWheels;
	}

	@Override
	public void execute() {
		// Localize and find amp
		// Pivot to amp position
		// Outtake into amp
	}

	@Override
	public void end(boolean interrupted) {
		// Stop outtake
	}

	@Override
	public void initialize() {

	}

	@Override
	public boolean isFinished() {
		// returns true or false based on status
		return true;
	}
}
