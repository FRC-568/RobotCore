package frc.team568.robot;

import edu.wpi.first.wpilibj.RobotBase;
import frc.team568.robot.rechargemodified.Robot;

public final class Main {
	
	private Main() {}

	public static void main(String[] args) {
		RobotBase.startRobot(Robot::new);
	}

}
