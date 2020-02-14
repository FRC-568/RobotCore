package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TalonDrive extends SubsystemBase {
	public final DifferentialDrive drive;

	public TalonDrive() {
		drive = null;
	}
}
