package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;

public class Barrel extends SequentialCommandGroup {
	
	public Barrel(RechargeDrive drive) {

		addCommands(

			// Turn at beginning
			new DriveDistance(drive, -2.5, 2.5, 0.1),

			// Move around D5
			new DriveDistance(drive, 150, 150, 0.3),
			new DriveDistance(drive, 20.7, -20.7, 0.2),
			new DriveDistance(drive, 80, 80, 0.3),
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 80, 80, 0.3),
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, 20.5, -20.5, 0.2),

			// Move around B8
			new DriveDistance(drive, 170, 170, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 80, 80, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 100, 100, 0.3),
			new DriveDistance(drive, -24, 24, 0.2),

			// Move around D10
			new DriveDistance(drive, 150, 150, 0.3),
			new DriveDistance(drive, -24, 24, 0.2),
			new DriveDistance(drive, 80, 80, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),

			// Move to finish zone
			new DriveDistance(drive, 200, 200, 0.3)

		);


	}

}
