package frc.team568.robot.powerup;

import frc.team568.robot.subsystems.WinchClimber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimbWithWinch extends CommandBase {
	public WinchClimber winchClimber;

	public ClimbWithWinch() {
		winchClimber = Robot.getInstance().climber;
	}

	@Override
	public void execute() {
		winchClimber.lift_m.set(-1);
	}

	@Override
	public void end(boolean interrupted) {
		winchClimber.lift_m.set(0);
	}

}
