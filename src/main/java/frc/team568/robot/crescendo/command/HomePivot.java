package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class HomePivot extends Command {
	PivotSubsystem pivot;

	public HomePivot(PivotSubsystem pivot){
		this.pivot = pivot;
		addRequirements(pivot);
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		pivot.setVoltage(-2);
	}

	@Override
	public boolean isFinished() {
		return pivot.getSwitch();
	}

	@Override
	public void end(boolean interrupted) {
		
	}
}
