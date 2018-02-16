package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.Robot;
import org.usfirst.frc.team568.robot.subsystems.BlockLift2018;

import edu.wpi.first.wpilibj.command.Command;

public class LiftBlock extends Command {
	public BlockLift2018 blockLift;

	public LiftBlock() {
		blockLift = Robot.getInstance().blockLift;

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		blockLift.lift_m.set(-1);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		blockLift.lift_m.set(0);

	}

	@Override
	protected void interrupted() {
		end();

	}

}
