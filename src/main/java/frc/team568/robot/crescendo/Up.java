package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;

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

	public boolean isFinished() {
		return false;
	}
}
