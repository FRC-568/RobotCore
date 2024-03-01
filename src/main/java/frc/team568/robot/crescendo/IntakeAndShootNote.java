package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class IntakeAndShootNote extends Command{
	JukeboxSubsystem intakeWheels;
	JukeboxSubsystem pivot;
	JukeboxSubsystem outTakeWheels;

	public IntakeAndShootNote(JukeboxSubsystem intakeWheels, JukeboxSubsystem pivot, JukeboxSubsystem outTakeWheels) {
		this.intakeWheels = intakeWheels;
		this.pivot = pivot;
		this.outTakeWheels = outTakeWheels;
	}

	@Override
	public void execute() {
		// Check if note is in intake
		// Pivot to shooting position
		// Check to see if in the right spot
		// Power outtake motors
	}

	@Override
	public void end(boolean interrupted) {
		// Stop Outtake Motors
	}

	@Override
	public void initialize() {
		// Power intake motors
	}

	@Override
	public boolean isFinished() {
		// return true or false based on status
		return true;
	}
}
