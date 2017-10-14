package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.Climb;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {

	public Solenoid reacherIn;
	public Solenoid reacherOut;
	public Solenoid topClampOut;
	public Solenoid topClampIn;
	public Solenoid bottomClampIn;
	public Solenoid bottomClampOut;
	public boolean isClimbing = false;
	public State currentState;

	public Climber() {

		// topClampIn = new Solenoid(RobotMap.topClampIn);
		// bottomClampIn = new Solenoid(RobotMap.bottomClampIn);
		// reacherIn = new Solenoid(RobotMap.reacherIn);
		// topClampOut = new Solenoid(RobotMap.topClampOut);
		// bottomClampOut = new Solenoid(RobotMap.bottomClampOut);
		reacherOut = new Solenoid(RobotMap.reacherOut);

		currentState = State.RELAXED;

		Robot.getInstance().oi.climb.whileHeld(new Climb(this));

	}

	@Override
	protected void initDefaultCommand() {

	}

	public static enum State {
		RELAXED, BOTTOM_CLAMPED, TOP_RELEASED, REACHED, TOP_CLAMPED, BOTTOM_RELEASED, LIFTED
	}

}
