package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoTaxi extends SequentialCommandGroup{
	AutoTaxi(MecanumSubsystem drive, double taxiTime){
		addRequirements(drive);
		addCommands(
			new WaitCommand(taxiTime),
			new RunCommand(() -> drive.driveStraight(0.7)).withTimeout(taxiTime)
		);
	}
}
