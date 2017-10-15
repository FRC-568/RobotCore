package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PortMapper;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Shooter2017 extends SubsystemBase {
	public SpeedController shootMotor;
	public Servo gate;
	public Solenoid ballWranglerIn;
	public Solenoid ballWranglerOut;

	public Shooter2017(PortMapper ports) {
		super(ports);
		
		shootMotor = new Spark(port("shooter"));
		gate = new Servo(port("gateServo"));
		ballWranglerIn = new Solenoid(port("ballWranglerIn"));
		ballWranglerOut = new Solenoid(port("ballWranglerOut"));
	}

}
