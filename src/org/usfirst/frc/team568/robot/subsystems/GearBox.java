package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearBox extends SubsystemBase {
	public Solenoid gearPneumatic1;
	public Solenoid gearPneumatic2;

	public DigitalInput gearDetector;

	public GearBox(final RobotBase robot) {
		super(robot);
		
		gearPneumatic1 = new Solenoid(port("gearPneumatic1"));
		gearPneumatic2 = new Solenoid(port("gearPneumatic2"));
		gearDetector = new DigitalInput(port("gearDetector"));
	}

	public void open() {
		gearPneumatic1.set(false);
		gearPneumatic2.set(true);
	}

	public void close() {
		gearPneumatic1.set(true);
		gearPneumatic2.set(false);
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

	public void hasGear() {
		SmartDashboard.putBoolean("Has Gear", gearDetector.get());
	}

}
