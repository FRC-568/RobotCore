package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class WinchClimber {
	public SpeedController climber;

	public WinchClimber() {
		climber = new Spark(RobotMap.climber);

	}

}
