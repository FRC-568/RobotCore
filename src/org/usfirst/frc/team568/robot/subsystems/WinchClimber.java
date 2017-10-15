package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PortMapper;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class WinchClimber extends SubsystemBase {
	public SpeedController climber;

	public WinchClimber(PortMapper ports) {
		super(ports);
		
		climber = new Spark(port("climber"));
	}

}
