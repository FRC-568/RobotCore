package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;

public class Bounce extends SequentialCommandGroup {
	
	public Bounce(RechargeDrive drive) {

		addCommands(
			
			// Move to column 3 and turn
			new DriveDistance(drive, 45, 45, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),

			// Move to A3 and come back
			new DriveDistance(drive, 45, 45, 0.3),
			new DriveDistance(drive, -45, -45, 0.3),

			// Move to column 6
			new DriveDistance(drive, -10.3, 10.3, 0.2),
			new DriveDistance(drive, -105, -105, 0.3),
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 64, 64, 0.3),
			new DriveDistance(drive, -10.3, 10.3, 0.2), 

			// Move to A6 and come back
			new DriveDistance(drive, 150, 150, 0.3),
			new DriveDistance(drive, -180, -180, 0.3),

			// Move to column 9
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 105, 105, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			
			// Move to A9 and move back
			new DriveDistance(drive, 150, 150, 0.3),
			new DriveDistance(drive, -80, -80, 0.3),

			// Move to finish zone
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 50, 50, 0.3)

		);

	}

}
