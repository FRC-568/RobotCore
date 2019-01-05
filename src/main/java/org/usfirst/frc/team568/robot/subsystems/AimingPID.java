package org.usfirst.frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class AimingPID extends PIDSubsystem {

	public AimingPID(double p, double i, double d) {
		super(p, i, d);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
