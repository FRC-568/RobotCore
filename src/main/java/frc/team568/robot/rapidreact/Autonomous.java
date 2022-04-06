package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Autonomous extends SequentialCommandGroup {
	protected MecanumSubsystem subsystem;
	protected Intake intake;

	public Autonomous(String autoType, MecanumSubsystem subsystem, Intake intake, AutonomousParameters param) {
		this.subsystem = subsystem;
		this.intake = intake;
		addRequirements(subsystem, intake);
		switch (autoType) {
			case "Outtake":
				addCommands(new ChargeAndScore(subsystem, intake, param));
				break;
			case "Taxi":
				addCommands(new AutoTaxi(subsystem, param.taxiTime()));
				break;
			case "Do Nothing":
				addCommands(new WaitCommand(100));
				break;
			default:
				addCommands(new WaitCommand(100));
		}
	}

	@Override
	public void end(boolean interrupted) {
		subsystem.getMecanumDrive().stopMotor();
		super.end(interrupted);
	}
}
