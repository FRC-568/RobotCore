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
		addRequirements(juke, pivot);
	}
	public Intake(JukeboxSubsystem juke) {
		this.juke = juke;
		addRequirements(juke);
	}

	@Override
	public void initialize() {
		speed = 0.5;
		angle = 0;
	}

	@Override
	public void execute() {
		juke.runIntake(speed);
		// pivot.setAngle(angle);
	}

	@Override
	public boolean isFinished() {
		return juke.hasNote();
	}

	@Override
	public void end(boolean interrupted) {
		juke.stopIntake();
	}
}
