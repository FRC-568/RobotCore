package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PortMapper;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearBox extends SubsystemBase {
	public Solenoid gearPneumatic1;
	public Solenoid gearPneumatic2;

	public DigitalInput gearDetector;

	public GearBox(PortMapper ports) {
		super(ports);
		
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
