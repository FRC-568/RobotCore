package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;

public class RopeCollector {

	public Solenoid ropeClampIn;
	public Solenoid ropeClampOut;

	public RopeCollector() {
		ropeClampIn = new Solenoid(RobotMap.ropeClampIn);
		ropeClampOut = new Solenoid(RobotMap.ropeClampOut);

		Robot.getInstance().oi.openRopeClamp.whenPressed(this.openCommand());
		Robot.getInstance().oi.closeRopeClamp.whenPressed(this.closeCommand());
	}

	public void open() {
		ropeClampIn.set(true);
		ropeClampOut.set(false);

	}

	public void close() {
		ropeClampIn.set(false);
		ropeClampOut.set(true);
	}

	public Command openCommand() {

		return new Command() {
			@Override
			public void initialize() {
				open();
			}

			@Override
			protected boolean isFinished() {
				// TODO Auto-generated method stub
				return true;
			}

		};

	}

	public Command closeCommand() {

		return new Command() {
			@Override
			public void initialize() {
				close();
			}

			@Override
			protected boolean isFinished() {
				// TODO Auto-generated method stub
				return true;
			}

		};

	}

	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
