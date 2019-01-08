package frc.team568.robot.commands;

import frc.team568.robot.powerup.Robot;
import frc.team568.robot.subsystems.WinchClimber;

import edu.wpi.first.wpilibj.command.Command;

public class UnClimb extends Command {
	public WinchClimber winchClimber;

	public UnClimb() {
		winchClimber = Robot.getInstance().climber;

	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		winchClimber.lift_m.set(1);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		winchClimber.lift_m.set(0);

	}

	@Override
	protected void interrupted() {
		end();

	}

}
