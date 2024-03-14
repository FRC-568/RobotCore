package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class IntakeNote extends Command{
	JukeboxSubsystem intakeWheels;

	public IntakeNote(JukeboxSubsystem intakeWheels) {
		this.intakeWheels = intakeWheels;
	}
	
	@Override
	public void execute() {
		intakeWheels.runIntake(0.5);
	}

	@Override
	public void end(boolean interrupted) {
		intakeWheels.stopIntake();
	}
	
	@Override
	public void initialize() {
		intakeWheels.runIntake();

		// Find position
	}

	@Override
	public boolean isFinished() {
		return intakeWheels.hasNote();
	}

}
