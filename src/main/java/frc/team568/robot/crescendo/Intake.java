package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;

public class Intake extends Command {
	JukeboxSubsystem juke;
	PivotSubsystem pivot;
	
	double speed;
	int angle;


	Intake(JukeboxSubsystem juke, PivotSubsystem pivot){
		this.juke = juke;
		this.pivot = pivot;
	}

	@Override
	public void initialize() {
		speed = 1;
		angle = 0;
	}

	@Override
	public void execute() {
		juke.setIntakeSpeed(speed);
		pivot.setAngle(angle);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		speed = 0;
		juke.setIntakeSpeed(speed);
	}
}
