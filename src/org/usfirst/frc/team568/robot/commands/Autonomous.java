package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous extends CommandGroup {
	protected SpeedController leftFront, leftBack, rightFront, rightBack;

	private double time = SmartDashboard.getNumber("How Long?");
	private boolean forward = SmartDashboard.getBoolean("Forward");

	public Autonomous() {

		// TODO Auto-generated constructor stub
	}

}
