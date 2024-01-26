package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;

public class Closing extends Command {
	PivotSubsystem pivot;
	
	int angle;
	Closing(PivotSubsystem pivot){
		this.pivot = pivot;
	}

	@Override
	public void initialize() {
		angle = 45;
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
