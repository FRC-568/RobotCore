package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.Robot;
import org.usfirst.frc.team568.robot.subsystems.BlockHandler;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class SpinBlockR extends Command {
	BlockHandler blockIntake;

	public SpinBlockR() {

	}

	protected void initialize() {
		blockIntake = Robot.getInstance().blockIntake;
	}

	protected void execute() {
		blockIntake.blockLiftIn();
		blockIntake.blockSpinR();
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
