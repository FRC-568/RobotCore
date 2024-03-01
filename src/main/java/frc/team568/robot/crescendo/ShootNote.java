package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class ShootNote extends Command{
	JukeboxSubsystem pivot;
	JukeboxSubsystem outTakeWheel;

	public ShootNote(JukeboxSubsystem pivot, JukeboxSubsystem outTakeWheel) {
		this.pivot = pivot;
		this.outTakeWheel = outTakeWheel;
	}

	@Override
	public void execute() {
		// Pivot to shooting position
		// Run outtake motors
	}

	@Override
	public void end(boolean interrupted) {
		// Stop outtake motors
	}

	@Override
	public void initialize() {
		// Find position
	}

	@Override
	public boolean isFinished() {
		// returns true or false based on status
		return true;
	}
}
