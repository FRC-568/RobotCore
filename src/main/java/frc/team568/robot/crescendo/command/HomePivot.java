package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class HomePivot extends Command {
	PivotSubsystem pivot;

	public HomePivot(PivotSubsystem pivot){
		this.pivot = pivot;
	}

	@Override
	public void initialize() {
		pivot.setVoltage(1);
	}

	@Override
	public void execute() {
	}

	@Override
	public boolean isFinished() {
		return pivot.getSwitch();
	}

	@Override
	public void end(boolean interrupted) {
	}
}
