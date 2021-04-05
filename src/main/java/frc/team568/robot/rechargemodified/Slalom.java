package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team568.robot.commands.DriveDistance;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Slalom extends SequentialCommandGroup {
	
	public Slalom(TwoMotorDrive drive) {

		addCommands(
			
			// Turn and move forward
			new DriveDistance(drive, -10.3, 10.3, 0.2),
			new DriveDistance(drive, 85, 85, 0.3),

			// Turn and go around row D
			new DriveDistance(drive, 10.3, -10.3, 0.2),
			new DriveDistance(drive, 150, 150, 0.3),

			// Go around D10
			new DriveDistance(drive, 20.5, -20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, -20.5, 20.5, 0.2),
			new DriveDistance(drive, 70, 70, 0.3),
			new DriveDistance(drive, 20.5, -20.5, 0.2),

			// Move straight below row D
			new DriveDistance(drive, 150, 150, 0.3),
			
			// Turn and move to finish zone
			new DriveDistance(drive, 10.3, -10.3, 0.2),
			new DriveDistance(drive, 85, 85, 0.3)

		);

	}

}
