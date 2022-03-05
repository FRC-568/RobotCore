package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class AutoTaxi extends SequentialCommandGroup {
	protected MecanumSubsystem subsystem;

	public AutoTaxi(MecanumSubsystem subsystem) {
		this.subsystem = subsystem;
		addRequirements(subsystem);
		addCommands(
				new WaitUntilCommand(10),
				new RunCommand(() -> {
					subsystem.getMecanumDrive().driveCartesian(0, 1, 0); // to move
				}).withTimeout(1));
	}

	@Override
	public void end(boolean interrupted) {
		subsystem.getMecanumDrive().driveCartesian(0, 0, 0);
		super.end(interrupted);
	}

	@Override
	public void execute() {
		super.execute();
	}

	@Override
	public void initialize() {
		super.initialize();
	}

	@Override
	public boolean isFinished() {
		return super.isFinished();
	}
}
