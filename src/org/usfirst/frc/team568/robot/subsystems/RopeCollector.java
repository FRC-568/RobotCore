package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.steamworks.Robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;

public class RopeCollector extends SubsystemBase {
	public Solenoid ropeClampIn;
	public Solenoid ropeClampOut;

	public RopeCollector(final RobotBase robot) {
		super(robot);
		
		ropeClampIn = new Solenoid(port("ropeClampIn"));
		ropeClampOut = new Solenoid(port("ropeClampOut"));

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
				return true;
			}
		};
	}

}