package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PortMapper;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PistonRopeClimber extends Subsystem {
	public State currentState;
	public final Clamp topClamp;
	public final Clamp bottomClamp;

	private final Solenoid reacherExtender;
	private final Solenoid reacherRetractor;
	private boolean _isReaching;

	public PistonRopeClimber(PortMapper map) {
		topClamp = new Clamp(map.getPort("topClampOut"), map.getPort("topClampIn"));
		bottomClamp = new Clamp(map.getPort("bottomClampOut"), map.getPort("bottomClampIn"));
		reacherExtender = new Solenoid(map.getPort("reacherOut"));
		reacherRetractor = new Solenoid(map.getPort("reacherIn"));

		bottomClamp.release();
		topClamp.release();
		retractReacher();
		currentState = State.RELAXED;
	}

	@Override
	protected void initDefaultCommand() {

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
