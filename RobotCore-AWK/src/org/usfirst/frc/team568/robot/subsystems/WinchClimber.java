package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.BringTheRobotDown;
import org.usfirst.frc.team568.robot.commands.ClimbWithWinch;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class WinchClimber {
	public SpeedController climber;

	public WinchClimber() {
		climber = new Victor(RobotMap.climber);

		Robot.getInstance().oi.climb.whileHeld(new ClimbWithWinch());
		Robot.getInstance().oi.inverseClimb.whileHeld(new BringTheRobotDown());
	}

}
