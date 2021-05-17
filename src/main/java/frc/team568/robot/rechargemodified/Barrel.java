package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Barrel extends SequentialCommandGroup {
	
	public Barrel(TwoMotorDrive drive) {

		addCommands(

			// Move around D5
			new DriveDistance(drive, 150, 150, 0.3),
			new DriveDistance(drive, 20.3, -20.3, 0.2),
			new DriveDistance(drive, 60, 60, 0.3),
			new DriveDistance(drive, 20.3, -20.3, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, 20.3, -20.3, 0.2),
			new DriveDistance(drive, 60, 60, 0.3),
			new DriveDistance(drive, 20.4, -20.4, 0.2),

			// Move around B8
			new DriveDistance(drive, 170, 170, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20, 20, 0.2),

			// Move around D10
			new DriveDistance(drive, 135, 135, 0.3),
			new DriveDistance(drive, -20.2, 20.2, 0.2),
			new DriveDistance(drive, 125, 125, 0.3),
			new DriveDistance(drive, -20.1, 20.1, 0.2),
			new DriveDistance(drive, 72, 72, 0.3),
			new DriveDistance(drive, -19.4, 19.4, 0.2),

			// Move to finish zone
			new DriveDistance(drive, 300, 300, 0.3)

		);


	}

}
