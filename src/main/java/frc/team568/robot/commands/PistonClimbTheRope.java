package frc.team568.robot.commands;

import frc.team568.robot.subsystems.PistonRopeClimber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class PistonClimbTheRope extends Command {
	protected final PistonRopeClimber climber;

	public PistonClimbTheRope(PistonRopeClimber climber) {
		this.climber = climber;
		requires(climber);
	}

	@Override
	protected void initialize() {
		switch (climber.currentState) {
		case RELAXED:
			climber.bottomClamp.grip();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.BOTTOM_CLAMPED;
			break;
		case BOTTOM_CLAMPED:
			climber.topClamp.release();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.TOP_RELEASED;
			break;
		case TOP_RELEASED:
			climber.extendReacher();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.REACHED;
			break;
		case REACHED:
			climber.topClamp.grip();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.TOP_CLAMPED;
			break;
		case TOP_CLAMPED:
			climber.bottomClamp.release();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.BOTTOM_RELEASED;
			break;
		case BOTTOM_RELEASED:
			climber.retractReacher();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.LIFTED;
			break;
		case LIFTED:
			climber.bottomClamp.grip();
			Timer.delay(.75);
			climber.currentState = PistonRopeClimber.State.BOTTOM_CLAMPED;
			break;
		default:
			break;
		}

	}

	@Override
	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		climber.topClamp.grip();
		climber.bottomClamp.grip();
		climber.currentState = PistonRopeClimber.State.BOTTOM_CLAMPED;
	}

	@Override
	protected void interrupted() {
		end();
	}

}
