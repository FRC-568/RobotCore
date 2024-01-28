package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class ScoreAmp extends Command {
	JukeBoxSubsystem jukebox;
	PivotSubsystem pivot;
	private double initTime;

	ScoreAmp(JukeBoxSubsystem jukebox, PivotSubsystem pivot) {
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
		final double power = 0.5;
		pivot.setAngle(60);
		jukebox.setIntakeSpeed(0);
		jukebox.setOuttakeSpeed(power, power);
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
