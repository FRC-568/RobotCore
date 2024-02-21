package frc.team568.robot.crescendo.command;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class Aim extends Command{
	
	PivotSubsystem pivot;
	
	DoubleSupplier distance;

	public Aim(PivotSubsystem pivot){
		this.pivot = pivot;
		distance = () -> Math.random();
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		pivot.aim(distance.getAsDouble());
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		
	}
}
