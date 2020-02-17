package frc.team568.robot.steamworks;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

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
		return new InstantCommand(this::open);
	}

	public Command closeCommand() {
		return new InstantCommand(this::close);
	}

	public void hasGear() {
		SmartDashboard.putBoolean("Has Gear", gearDetector.get());
	}

}
