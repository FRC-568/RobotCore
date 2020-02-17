package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class PistonClimbTheRope extends CommandBase {
	protected final PistonRopeClimber climber;

	public PistonClimbTheRope(PistonRopeClimber climber) {
		this.climber = climber;
		addRequirements(climber);
	}

	@Override
	public void initialize() {
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
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end(boolean interrupted) {
		climber.topClamp.grip();
		climber.bottomClamp.grip();
		climber.currentState = PistonRopeClimber.State.BOTTOM_CLAMPED;
	}

}
