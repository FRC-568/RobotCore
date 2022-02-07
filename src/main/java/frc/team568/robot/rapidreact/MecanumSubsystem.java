package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class MecanumSubsystem extends SubsystemBase {
	private MecanumDrive drive;
	private WPI_TalonSRX motorFL, motorFR, motorBL, motorBR;

	public MecanumSubsystem(RobotBase robot) {
		super(robot);

		motorBL = new WPI_TalonSRX(1);
		motorFL = new WPI_TalonSRX(2);
		motorBR = new WPI_TalonSRX(3);
		motorFR = new WPI_TalonSRX(4);

		drive = new MecanumDrive(motorFL, motorBL, motorFR, motorBR);
	}
	
	public MecanumDrive getMecanumDrive() {
		return drive;
	}
}