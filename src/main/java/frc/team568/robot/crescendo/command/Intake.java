package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class Intake extends Command {
	JukeboxSubsystem juke;
	PivotSubsystem pivot;
	
	private static final double intakeSpeed = 1.0;
	private static final double pivotAngle = 0.0;

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
	public void execute() {
		juke.runIntake(intakeSpeed);
		if (pivot != null)
			pivot.setAngle(pivotAngle);
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
