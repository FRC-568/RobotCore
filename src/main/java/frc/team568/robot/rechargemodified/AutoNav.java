package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;
import frc.team568.robot.commands.Turn;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class AutoNav extends SequentialCommandGroup {
	
	public AutoNav(TwoMotorDrive drive) {

		addCommands(
		
			new DriveDistance(drive, 50, 50, 1),
			new Turn(drive, 90, 1)

		);

	}

}
