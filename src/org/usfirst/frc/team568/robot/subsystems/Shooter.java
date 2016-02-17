package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.commands.DoNotShoot;
import org.usfirst.frc.team568.robot.commands.GetBall;
import org.usfirst.frc.team568.robot.commands.Shoot;
import org.usfirst.frc.team568.robot.commands.TiltDownwards;
import org.usfirst.frc.team568.robot.commands.TiltUpwards;

public class Shooter {

	public Shooter() {
		// TODO Auto-generated constructor stub
		Robot.getInstance().oi.rightThree.whenPressed(new Shoot());
		Robot.getInstance().oi.rightFive.whenPressed(new GetBall());
		Robot.getInstance().oi.rightTwo.whenPressed(new DoNotShoot());
		Robot.getInstance().oi.leftFive.whenPressed(new TiltDownwards());
		Robot.getInstance().oi.leftThree.whenPressed(new TiltUpwards());
	}

}
