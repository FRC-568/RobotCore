package org.usfirst.frc.team568.robot.stronghold;

import org.usfirst.frc.team568.robot.subsystems.Arms;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class AutoArm extends TimedCommand {
	private final Arms arm;
	
	public AutoArm(Arms arm) {
		super(2.0);
		this.arm = arm;
	}

	@Override
	protected void execute() {
		arm.goDown();
	}

	@Override
	protected void end() {
		arm.stop();
	}

}
