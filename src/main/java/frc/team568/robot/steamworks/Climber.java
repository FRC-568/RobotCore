package frc.team568.robot.steamworks;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.Solenoid;

public class Climber extends SubsystemBase {

	public Solenoid reacherIn;
	public Solenoid reacherOut;
	public Solenoid topClampOut;
	public Solenoid topClampIn;
	public Solenoid bottomClampIn;
	public Solenoid bottomClampOut;
	public boolean isClimbing = false;
	public State currentState;

	public Climber(RobotBase robot) {
		super(robot);
		// topClampIn = new Solenoid(RobotMap.topClampIn);
		// bottomClampIn = new Solenoid(RobotMap.bottomClampIn);
		// reacherIn = new Solenoid(RobotMap.reacherIn);
		// topClampOut = new Solenoid(RobotMap.topClampOut);
		// bottomClampOut = new Solenoid(RobotMap.bottomClampOut);
		reacherOut = new Solenoid(port("reacherOut"));

		currentState = State.RELAXED;

		//command should be added from the Robot
		//Robot.getInstance().oi.climb.whileHeld(new Climb(this));

	}

	public static enum State {
		RELAXED, BOTTOM_CLAMPED, TOP_RELEASED, REACHED, TOP_CLAMPED, BOTTOM_RELEASED, LIFTED
	}

}
