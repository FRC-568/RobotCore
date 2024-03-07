package frc.team568.robot.crescendo.command;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class SpinUp extends Command{
	private static final double time = 1;
	
	private JukeboxSubsystem jukebox;
	private double initTime;
	public SpinUp(JukeboxSubsystem jukebox){
		this.jukebox = jukebox;
	}

	@Override
	public void initialize() {
		initTime = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
		double difference = 0.3;
		jukebox.setOuttakeSpeed(1, 1 - difference);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		jukebox.setOuttakeSpeed(0);
	}
}
