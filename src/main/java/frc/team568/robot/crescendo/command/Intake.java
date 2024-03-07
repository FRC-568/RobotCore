package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class Intake extends Command {
	JukeboxSubsystem juke;
	PivotSubsystem pivot;
	
	double speed;
	int angle;

	public Intake(JukeboxSubsystem juke, PivotSubsystem pivot) {
		this.juke = juke;
		this.pivot = pivot;
	}
	public Intake(JukeboxSubsystem juke) {
		this.juke = juke;
	}

	@Override
	public void initialize() {
		speed = 0.5;
		angle = 0;
	}

	@Override
	public void execute() {
		juke.setIntakeSpeed(speed);
		// pivot.setAngle(angle);
	}

	@Override
	public boolean isFinished() {
		return juke.hasNote();
	}

	@Override
	public void end(boolean interrupted) {
		speed = 0;
		juke.setIntakeSpeed(speed);
	}
}
