package frc.team568.robot.commands;

import frc.team568.robot.steamworks.ControllerButtons;
import frc.team568.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

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
		// reacherIn();
		// topClampIn();
		// bottomClampIn();
	}

	@Override
	protected void execute() {
		switch (climber.currentState) {
		case RELAXED:
			bottomClampOut();
			Timer.delay(1);
			climber.currentState = Climber.State.BOTTOM_CLAMPED;
			break;
		case BOTTOM_CLAMPED:
			topClampIn();
			Timer.delay(1);
			climber.currentState = Climber.State.TOP_RELEASED;
			break;
		case TOP_RELEASED:
			reacherOut();
			Timer.delay(1);
			climber.currentState = Climber.State.REACHED;
			break;
		case REACHED:
			topClampOut();
			Timer.delay(1);
			climber.currentState = Climber.State.TOP_CLAMPED;
			break;
		case TOP_CLAMPED:
			bottomClampIn();
			Timer.delay(1);
			climber.currentState = Climber.State.BOTTOM_RELEASED;
			break;
		case BOTTOM_RELEASED:
			reacherIn();
			Timer.delay(1);
			climber.currentState = Climber.State.LIFTED;
			break;
		case LIFTED:
			bottomClampOut();
			Timer.delay(1);
			climber.currentState = Climber.State.BOTTOM_CLAMPED;
			break;

		default:
			break;
		}
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
		climber.currentState = Climber.State.BOTTOM_CLAMPED;
		// reacher
	}

	@Override
	protected void interrupted() {
		topClampOut();
		bottomClampOut();
		// climber.isClimbing = false;
		// climber.currentState = Climber.State.BOTTOM_CLAMPED;
		// reacher
	}

}
