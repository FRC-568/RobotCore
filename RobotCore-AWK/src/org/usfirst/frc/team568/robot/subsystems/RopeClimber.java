package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class RopeClimber extends Subsystem {
	public State currentState;
	public Clamp topClamp;
	public Clamp bottomClamp;

	private Solenoid reacherExtender;
	private Solenoid reacherRetractor;
	private boolean _isReaching;

	public RopeClimber() {
		topClamp = new Clamp(RobotMap.topClampOut, RobotMap.topClampIn);
		bottomClamp = new Clamp(RobotMap.bottomClampOut, RobotMap.bottomClampIn);
		reacherExtender = new Solenoid(RobotMap.reacherOut);
		reacherRetractor = new Solenoid(RobotMap.reacherIn);

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
