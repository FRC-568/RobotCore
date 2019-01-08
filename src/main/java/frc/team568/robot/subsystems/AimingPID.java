package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class AimingPID extends PIDSubsystem {

	public AimingPID(double p, double i, double d) {
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
	protected void initDefaultCommand() {
	}

}
