package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearBox extends Subsystem {
	public Solenoid gearPneumatic1;
	public Solenoid gearPneumatic2;

	public DigitalInput gearDetector;

	public GearBox() {
		gearPneumatic1 = new Solenoid(RobotMap.gearPneumatic1);
		gearPneumatic2 = new Solenoid(RobotMap.gearPneumatic2);

		gearDetector = new DigitalInput(RobotMap.gearDetector);

		Robot.getInstance().oi.openGearBox.whenPressed(this.openCommand());
		Robot.getInstance().oi.closeGearBox.whenPressed(this.closeCommand());
	}

	public void open() {
		gearPneumatic1.set(true);
		gearPneumatic2.set(false);

	}

	public void close() {
		gearPneumatic1.set(false);
		gearPneumatic2.set(true);
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

	public void hasGear() {
		if (gearDetector.get()) {
			SmartDashboard.putBoolean("Has Gear", true);
		} else {
			SmartDashboard.putBoolean("Has Gear", false);
		}
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
