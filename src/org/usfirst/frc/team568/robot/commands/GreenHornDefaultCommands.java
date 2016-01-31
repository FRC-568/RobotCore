package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.ControllerButtons;
import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.GreenHorn;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class GreenHornDefaultCommands extends Command {
	GreenHorn shooter;
	Joystick joy;

	public GreenHornDefaultCommands() {
		shooter = Robot.getInstance().shooter;
		requires(shooter);
		joy = Robot.getInstance().oi.rightStick;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		if (joy.getRawButton(ControllerButtons.shootBallButton) == true) {
			shooter.shootBall(1);
		} else if (joy.getRawButton(ControllerButtons.obtainBallButton) == true) {
			shooter.obtainBall(.7);
		} else {
			shooter.setSpeed(0);
			shooter.nudgerNeutral();
		}

		if (joy.getRawButton(ControllerButtons.aimPickUpBallButton) == true) {
			shooter.goToPositionLoad();
		} else if (joy.getRawButton(ControllerButtons.aimShootLowGoalButton) == true) {
			shooter.goToPositionShootLow(.4);
		} else if (joy.getRawButton(ControllerButtons.aimShootHighGoalButton) == true) {
			shooter.goToPositionShootHigh();
		}
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
