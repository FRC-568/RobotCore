package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.ControllerButtons;
import org.usfirst.frc.team568.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Climb extends Command {
	public Climber climber;
	public Joystick controller2;
	public ControllerButtons buttons;

	public Climb(Climber climber) {
		System.out.println("GoToCommand");
		// climber.isClimbing = true;
		this.climber = climber;

	}

	public void topClampIn() {

		climber.topClampIn.set(true);
		climber.topClampOut.set(false);
	}

	public void topClampOut() {
		climber.topClampIn.set(false);
		climber.topClampOut.set(true);
	}

	public void bottomClampIn() {
		climber.bottomClampIn.set(true);
		climber.bottomClampOut.set(false);
	}

	public void bottomClampOut() {
		climber.bottomClampIn.set(false);
		climber.bottomClampOut.set(true);
	}

	public void reacherIn() {
		climber.reacherIn.set(true);
		climber.reacherOut.set(false);
	}

	public void reacherOut() {
		climber.reacherIn.set(false);
		climber.reacherOut.set(true);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		switch (climber.currentState) {
		case RELAXED:
			bottomClampOut();
			Timer.delay(.75);
			climber.currentState = Climber.State.BOTTOM_CLAMPED;
			break;
		case BOTTOM_CLAMPED:
			topClampIn();
			Timer.delay(.75);
			climber.currentState = Climber.State.TOP_RELEASED;
			break;
		case TOP_RELEASED:
			reacherOut();
			Timer.delay(.75);
			climber.currentState = Climber.State.REACHED;
			break;
		case REACHED:
			topClampOut();
			Timer.delay(.75);
			climber.currentState = Climber.State.TOP_CLAMPED;
			break;
		case TOP_CLAMPED:
			bottomClampIn();
			Timer.delay(.75);
			climber.currentState = Climber.State.BOTTOM_RELEASED;
			break;
		case BOTTOM_RELEASED:
			reacherIn();
			Timer.delay(.75);
			climber.currentState = Climber.State.LIFTED;
			break;
		case LIFTED:
			bottomClampOut();
			Timer.delay(.75);
			climber.currentState = Climber.State.RELAXED;
			break;

		default:
			break;
		}

	}

	@Override
	protected void execute() {
		bottomClampOut();
		Timer.delay(.5);
		reacherOut();
		Timer.delay(.5);
		topClampOut();
		Timer.delay(.5);
		bottomClampIn();
		Timer.delay(.5);
		reacherIn();
		Timer.delay(.5);
		bottomClampOut();
		Timer.delay(.5);
		topClampIn();
		Timer.delay(.5);
		reacherOut();
		Timer.delay(.5);
		topClampOut();
		Timer.delay(.5);
		bottomClampIn();
		Timer.delay(.5);
		reacherIn();
		Timer.delay(.5);

	}

	@Override
	protected boolean isFinished() {

		return false;
	}

	@Override
	protected void end() {
		topClampOut();
		bottomClampOut();
		climber.isClimbing = false;
		// reacher

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
