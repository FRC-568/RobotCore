package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoTaxi extends SequentialCommandGroup {
	protected MecanumSubsystem subsystem;
	protected Intake intake;
	double TaxiTime = 1.5;
	double lidTime = 1, intakeTime = 3;
	
	public AutoTaxi(MecanumSubsystem subsystem, Intake intake) {
		this.subsystem = subsystem;
		this.intake = intake;
		addRequirements(subsystem);
		addCommands(
			new InstantCommand(() -> intake.setLidOpen(false)),
			new WaitCommand(lidTime),
			new RunCommand(() -> intake.setIntakeMotor(1.0)).withTimeout(intakeTime),
			new WaitCommand(Math.max(1, 10 - lidTime - intakeTime)),
			new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(0, -1, 0)).withTimeout(TaxiTime),
			new WaitCommand(100)
		);
	}

	public AutoTaxi setTaxiTime(double TaxiTime){
		this.TaxiTime = TaxiTime;
		return this;
	}

	public AutoTaxi setLidTime(double lidTime){
		this.lidTime = lidTime;
		return this;
	}

	public AutoTaxi setIntakeTime(double intakeTime){
		this.intakeTime = intakeTime;
		return this;
	}

	@Override
	public void end(boolean interrupted) {
		subsystem.getMecanumDrive().stopMotor();
		super.end(interrupted);
	}
}
