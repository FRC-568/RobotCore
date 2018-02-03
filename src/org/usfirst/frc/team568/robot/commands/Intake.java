package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.steamworks.Robot;
import org.usfirst.frc.team568.robot.subsystems.BlockIntake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Intake extends Command {
	BlockIntake blockIntake;

	public Intake() {

	}

	protected void initialize() {
		blockIntake = Robot.getInstance().blockIntake;
	}

	protected void execute() {
		blockIntake.intakeOne.set(1);
		blockIntake.intakeTwo.set(-1);
		Timer.delay(.5);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void end() {
		blockIntake.intakeOne.set(0);
		blockIntake.intakeTwo.set(0);
	}

}