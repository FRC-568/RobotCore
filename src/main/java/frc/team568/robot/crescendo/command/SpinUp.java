package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class SpinUp extends Command {
	private final JukeboxSubsystem jukebox;

	public SpinUp(final JukeboxSubsystem jukebox) {
		addRequirements(jukebox);
		this.jukebox = jukebox;
	}

	@Override
	public void execute() {
		jukebox.runOuttake();
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		jukebox.stopOuttake();
	}
}
