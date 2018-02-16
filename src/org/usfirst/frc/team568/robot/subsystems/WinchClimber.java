package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class WinchClimber extends SubsystemBase {
	public SpeedController lift_m;

	public WinchClimber(final RobotBase robot) {
		super(robot);
		
		lift_m = new Spark(port("climber"));
	}

}
