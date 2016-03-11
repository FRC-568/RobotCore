package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmUP extends Command {

	public ArmUP() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() {
		Robot.getInstance().arms.GoUp();

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if (Robot.getInstance().oi.shootSix.get()) {
			// Robot.getInstance().arms.topLimmitSwitch.get()) {
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
		// TODO Auto-generated method stub
		Robot.getInstance().arms.Stop();
	}

}
