package frc.team568.robot.steamworks;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Shooter2017 extends SubsystemBase {
	public SpeedController shootMotor;
	public Servo gate;
	public Solenoid ballWranglerIn;
	public Solenoid ballWranglerOut;

	public Shooter2017(final RobotBase robot) {
		super(robot);
		
		shootMotor = new Spark(port("shooter"));
		gate = new Servo(port("gateServo"));
		ballWranglerIn = new Solenoid(port("ballWranglerIn"));
		ballWranglerOut = new Solenoid(port("ballWranglerOut"));
	}

}