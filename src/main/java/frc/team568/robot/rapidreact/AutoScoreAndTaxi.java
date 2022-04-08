package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoScoreAndTaxi extends SequentialCommandGroup{
	AutoScoreAndTaxi(MecanumSubsystem drive, Intake intake, AutonomousParameters param, BuiltInAccelerometer accel) {
		addRequirements(drive, intake);
		addCommands(
			intake.commandOpenLid(),
			new ChargeAndScore(drive, intake, param, accel),
			new InstantCommand(() -> drive.stop()),
			new WaitCommand(param.shootTime()),
			new InstantCommand(() -> intake.stop()),
			new AutoTaxi(drive, param.taxiTime()),
			new WaitCommand(100)
		);
	}
}
