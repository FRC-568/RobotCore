package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;

public class GyroSubsystem extends SubsystemBase {
	
	private ADXRS450_Gyro gyro;

	public GyroSubsystem(RobotBase robot) {
		super(robot);

		gyro = new ADXRS450_Gyro();
		addChild(gyro);
	}

	public Gyro getGyro() {
		return gyro;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Rate", gyro::getRate, null);
	}

}