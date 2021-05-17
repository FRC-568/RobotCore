package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Bounce extends SequentialCommandGroup {
	
	public Bounce(TwoMotorDrive drive) {

		addCommands(
			
			// Move to column 3 and turn
			new DriveDistance(drive, 45, 45, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),

			// Move to A3 and come back
			new DriveDistance(drive, 45, 45, 0.3),
			new DriveDistance(drive, -35, -35, 0.3),

			// Move to column 6
			new DriveDistance(drive, -9.7, 9.7, 0.2),
			new DriveDistance(drive, -108, -108, 0.3),
			new DriveDistance(drive, 20.3, -20.3, 0.2),
			new DriveDistance(drive, 42, 42, 0.3),
			new DriveDistance(drive, -10.1, 10.1, 0.2), 

			// Move to A6 and come back
			new DriveDistance(drive, 110, 110, 0.3),
			new DriveDistance(drive, -150, -150, 0.3),

			// Move to column 9
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 95, 95, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			
			// Move to A9 and move back
			new DriveDistance(drive, 120, 120, 0.3),
			new DriveDistance(drive, -50, -50, 0.3),

			// Move to finish zone
			new DriveDistance(drive, 20.7, -20.7, 0.2),
			new DriveDistance(drive, 50, 50, 0.3)

		);

	}

}
