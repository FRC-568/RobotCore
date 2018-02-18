package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.Robot;
import org.usfirst.frc.team568.robot.subsystems.BlockHandler;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class BlockIn extends Command {
	BlockHandler blockIntake;

	public BlockIn() {

	}

	protected void initialize() {
		blockIntake = Robot.getInstance().blockIntake;
	}

	protected void execute() {
		blockIntake.blockLiftIn();
		blockIntake.blockArmIn();
		Timer.delay(.1);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void end() {
		blockIntake.allStop();
	}

}
