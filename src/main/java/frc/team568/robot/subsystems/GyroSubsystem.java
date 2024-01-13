package frc.team568.robot.subsystems;

import edu.wpi.first.util.sendable.SendableRegistry;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.team568.robot.RobotBase;

public class GyroSubsystem extends SubsystemBase {
	
	private ADXRS450_Gyro gyro;

	public GyroSubsystem(RobotBase robot) {
		
		super(robot);
		gyro = new ADXRS450_Gyro();
		SendableRegistry.addChild(this, gyro);
	
	}

	public Gyro getGyro() {
		return (Gyro) gyro;
	}

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);
		builder.addDoubleProperty("Rate", gyro::getRate, null);
		builder.addDoubleProperty("Angle", gyro::getAngle, null);
	
	}

}