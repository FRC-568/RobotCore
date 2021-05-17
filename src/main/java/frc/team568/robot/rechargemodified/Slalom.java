package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Slalom extends SequentialCommandGroup {
	
	public Slalom(TwoMotorDrive drive) {

		addCommands(
			
			// Turn and move forward
			new DriveDistance(drive, 45, 45, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 58, 58, 0.3),
			new DriveDistance(drive, 20.6, -20.6, 0.2),

			// Go around row D
			new DriveDistance(drive, 210, 210, 0.3),

			// Go around D10
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 65, 65, 0.3),
			new DriveDistance(drive, -20.3, 20.3, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.3, 20.3, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.3, 20.3, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.3, 20.3, 0.2),
			new DriveDistance(drive, 69, 69, 0.3),
			new DriveDistance(drive, 20.95, -20.95, 0.2),

			// Move straight below row D
			new DriveDistance(drive, 183, 183, 0.3),
			
			// Turn and move to finish zone
			new DriveDistance(drive, 10, -10, 0.2),
			new DriveDistance(drive, 90, 90, 0.3)

		);

	}

}
