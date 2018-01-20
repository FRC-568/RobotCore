package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class WinchClimber extends SubsystemBase {
	public SpeedController climber;

	public WinchClimber(final RobotBase robot) {
		super(robot);
		
		climber = new Spark(port("climber"));
	}

}
