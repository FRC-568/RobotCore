package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class WinchClimber extends SubsystemBase {
	public MotorController lift_m;

	public WinchClimber(final RobotBase robot) {
		super(robot);

		lift_m = new WPI_TalonSRX(port("climber"));
	}

}
