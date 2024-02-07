package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class Up extends Command {
	PivotSubsystem pivot;

	int angle;

	Up(PivotSubsystem pivot){
		this.pivot = pivot;
	}

	@Override
	public void initialize() {
		angle = 90;
	}

	@Override
	public void execute() {
		pivot.setAngle(angle);
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
}
