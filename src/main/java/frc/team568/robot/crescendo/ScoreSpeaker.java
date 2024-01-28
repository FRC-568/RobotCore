package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class ScoreSpeaker extends Command {
	JukeBoxSubsystem jukebox;
	PivotSubsystem pivot;
	private double initTime;

	ScoreSpeaker(JukeBoxSubsystem jukebox, PivotSubsystem pivot) {
		addRequirements(jukebox);
		addRequirements(pivot);

		this.jukebox = jukebox;
		this.pivot = pivot;
	}

	@Override
	public void initialize() {
		initTime = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
		final double difference = 0.5;
		pivot.setAngle(0);
		jukebox.setIntakeSpeed(0);
		jukebox.setOuttakeSpeed(1, 1 - difference);
	}

	@Override
	public boolean isFinished() {
		
		return Timer.getFPGATimestamp() - initTime >= 2; // seconds, according to the javadoc. not sure if I belive that (check this if it doesn't run long enough)
	}

	@Override
	public void end(boolean interrupted) {
		jukebox.setIntakeSpeed(0);
		jukebox.setOuttakeSpeed(0, 0);
	}
}
