package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SubsystemBase extends Subsystem {
	public final RobotBase robot;

	public SubsystemBase(final RobotBase robot) {
		this.robot = robot;
	}

	public SubsystemBase(String name, final RobotBase robot) {
		super(name);
		this.robot = robot;
	}
	
	protected int port(String name) {
		return robot.getPort(name);
	}
	
	@Override
	protected void initDefaultCommand() {
	}

}
