package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class AutoTaxi extends SequentialCommandGroup{
	AutoTaxi(MecanumSubsystem drive, double taxiTime){
		addRequirements(drive);
		addCommands(
			new WaitUntilCommand(10),
			new RunCommand(() -> drive.driveStraight(0.7)).withTimeout(taxiTime)
		);
	}
}
