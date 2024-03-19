package frc.team568.robot.crescendo.command;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class Aim extends Command {
	PivotSubsystem pivot;
	DoubleSupplier distanceSupplier;

	public Aim(PivotSubsystem pivot) {
		addRequirements(pivot);
		this.pivot = pivot;
	}

	public Aim(PivotSubsystem pivot, DoubleSupplier distanceSupplier) {
		addRequirements(pivot);
		this.pivot = pivot;
		this.distanceSupplier = distanceSupplier;
	}

	@Override
	public void execute() {
		if (distanceSupplier != null)
			pivot.aim(distanceSupplier.getAsDouble());
		else
			pivot.aim(true); 
			// If this command is being used for both amp and speaker, 
			// The command needs a way to know which one (Speaker or Amp) is being aimed at.
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
