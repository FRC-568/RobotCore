package frc.team568.robot.crescendo.command;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class NoteRun extends Command{
	
	JukeboxSubsystem juke;
	
	public NoteRun(JukeboxSubsystem juke){
		this.juke = juke;
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		
	}
}
