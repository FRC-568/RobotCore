package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Solenoid;

public class PistonRopeClimber extends SubsystemBase {
	public State currentState;
	public final Clamp topClamp;
	public final Clamp bottomClamp;

	private final Solenoid reacherExtender;
	private final Solenoid reacherRetractor;
	private boolean _isReaching;

	public PistonRopeClimber(final RobotBase robot) {
		super(robot);
		topClamp = new Clamp(port("topClampOut"), port("topClampIn"));
		bottomClamp = new Clamp(port("bottomClampOut"), port("bottomClampIn"));
		reacherExtender = new Solenoid(port("reacherOut"));
		reacherRetractor = new Solenoid(port("reacherIn"));

		bottomClamp.release();
		topClamp.release();
		retractReacher();
		currentState = State.RELAXED;
	}

	public void extendReacher() {
		reacherExtender.set(true);
		reacherRetractor.set(false);
		_isReaching = true;
	}

	public void retractReacher() {
		reacherExtender.set(false);
		reacherRetractor.set(true);
		_isReaching = false;
	}

	public boolean isReaching() {
		return _isReaching;
	}

	public static enum State {
		RELAXED, BOTTOM_CLAMPED, TOP_RELEASED, REACHED, TOP_CLAMPED, BOTTOM_RELEASED, LIFTED
	}

	public final class Clamp {
		private Solenoid extend;
		private Solenoid retract;
		private boolean _isGripping;

		private Clamp(int extendPort, int retractPort) {
			extend = new Solenoid(extendPort);
			retract = new Solenoid(retractPort);
			_isGripping = false;
		}

		public void grip() {
			extend.set(true);
			retract.set(false);
		}

		public void release() {
			extend.set(false);
			retract.set(true);
		}

		public boolean isGripping() {
			return _isGripping;
		}
	}

}
