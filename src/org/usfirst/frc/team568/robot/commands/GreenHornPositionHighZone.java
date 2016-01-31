package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.GreenHorn;

import edu.wpi.first.wpilibj.command.Command;

public class GreenHornPositionHighZone extends Command {

	GreenHorn shooter;

	public GreenHornPositionHighZone() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		setTimeout(.33);
		shooter.goToPositionShootHigh();

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
