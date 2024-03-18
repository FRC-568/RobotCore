package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class IntakeNote extends Command{
	JukeboxSubsystem jukebox;

	public IntakeNote(JukeboxSubsystem jukebox) {
		this.jukebox = jukebox;
	}
	
	@Override
	public void execute() {
		jukebox.runIntake(0.5);
	}

	@Override
	public void end(boolean interrupted) {
		jukebox.stopIntake();
	}
	
	@Override
	public void initialize() {
		jukebox.runIntake();

		// Find position
	}

	@Override
	public boolean isFinished() {
		return jukebox.hasNote();
	}

} // Command isn't used, and may also be a duplicate of 'Intake.java'
