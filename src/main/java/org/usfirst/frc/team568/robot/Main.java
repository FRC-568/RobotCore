package org.usfirst.frc.team568.robot;

import edu.wpi.first.wpilibj.RobotBase;

import org.usfirst.frc.team568.robot.deepspace.Robot;

public final class Main {
	private Main() {}

	public static void main(String[] args) {
		RobotBase.startRobot(Robot::new);
	}
}
