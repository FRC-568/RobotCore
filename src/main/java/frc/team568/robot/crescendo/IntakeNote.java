package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class IntakeNote extends Command{
	// Step 1: Turn on Intake (from intake wheels)
	JukeboxSubsystem intakeWheels;

	public IntakeNote(JukeboxSubsystem intakeWheels) {
		this.intakeWheels = intakeWheels;
	}
	
	@Override
	public void execute() {

		// Find Note
		// Drive in
	}

	@Override
	public void end(boolean interrupted) {
		// Stop intake
		intakeWheels.setIntakeSpeed(0);

	}

	@Override
	public void initialize() {
		// Power intake
		intakeWheels.setIntakeSpeed(1);

		// Find position
	}

	@Override
	public boolean isFinished() {
		// returns true or false if finished
		return intakeWheels.hasNote();
	}

}
