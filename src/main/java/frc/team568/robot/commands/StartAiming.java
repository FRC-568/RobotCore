package frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class StartAiming extends PIDCommand {

	public StartAiming(double p, double i, double d) {
		super(p, i, d);
	}

	@Override
	protected double returnPIDInput() {
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
