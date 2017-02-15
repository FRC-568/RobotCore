package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.Shoot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
	public SpeedController shooter;

	public Shooter() {
		shooter = new Victor(RobotMap.shooter);

		Robot.getInstance().oi.stopShooting.whenPressed(new Shoot());
		Robot.getInstance().oi.shoot.cancelWhenPressed(new Shoot());

	}

}
