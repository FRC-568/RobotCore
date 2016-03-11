package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmDown extends Command {

	public ArmDown() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		Robot.getInstance().arms.GoDown();

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().oi.shootSeven.get()) {

			// arms.bottomLimmitSwitch.get()) {

			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		Robot.getInstance().arms.Stop();
	}

	@Override
	protected void interrupted() {
		Robot.getInstance().arms.Stop();
		// TODO Auto-generated method stub

	}

}
