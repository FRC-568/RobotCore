package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoTaxi extends SequentialCommandGroup {
	protected MecanumSubsystem subsystem;
	protected Intake intake;
	double taxiTime = 1.5;
	double lungeTime = 1, intakeTime = 3, lidDelay = 0.3;
	
	public AutoTaxi(MecanumSubsystem subsystem, Intake intake, boolean outTake) {
		this.subsystem = subsystem;
		this.intake = intake;
		addRequirements(subsystem, intake);
		if(outTake){
			addCommands(
				new InstantCommand(() -> intake.setLidOpen(true)),
				new ParallelCommandGroup(
					new InstantCommand(() -> intake.setIntakeMotor(-0.7)),
					new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(0.7, 0, 0)).withTimeout(lungeTime)),
					new WaitCommand(lidDelay).andThen(new InstantCommand(() -> intake.setLidOpen(false))
				),
				new WaitCommand(intakeTime),
				new InstantCommand(() -> subsystem.getMecanumDrive().driveCartesian(0, 0, 0)),
				new InstantCommand(() -> intake.setIntakeMotor(0.0)),
				new WaitCommand(Math.max(1, 10 - lungeTime - intakeTime)),
				new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(-0.5, 0, 0)).withTimeout(taxiTime),
				new WaitCommand(100)
			);
		} else {
			addCommands(
				new WaitCommand(10),
				new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(-0.5, 0, 0)).withTimeout(taxiTime),
				new WaitCommand(100)
			);
		}	
	}

	public AutoTaxi setLidDelay(double lidDelay){
		this.lidDelay = lidDelay;
		return this;
	}

	public AutoTaxi setTaxiTime(double TaxiTime){
		this.taxiTime = TaxiTime;
		return this;
	}

	public AutoTaxi setLidTime(double lungeTime){
		this.lungeTime = lungeTime;
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
