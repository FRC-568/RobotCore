package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class ScoreAmp extends Command {
	JukeboxSubsystem jukebox;
	PivotSubsystem pivot;
	Timer timer = new Timer();

	public ScoreAmp(JukeboxSubsystem jukebox, PivotSubsystem pivot) {
		addRequirements(jukebox, pivot);

		this.jukebox = jukebox;
		this.pivot = pivot;
	}

	@Override
	public void initialize() {
		timer.restart();
	}

	@Override
	public void execute() {
		final double power = 0.5;
		pivot.moveToward(60);
		jukebox.stopIntake();
		jukebox.runOuttakeManual(power, power);
	}

	@Override
	public boolean isFinished() {
		return timer.hasElapsed(2); // seconds, according to the javadoc. not sure if I belive that (check this if it doesn't run long enough)
	}

	@Override
	public void end(boolean interrupted) {
		timer.stop();
		jukebox.stopIntake();
		jukebox.stopOuttake();
	}
}
