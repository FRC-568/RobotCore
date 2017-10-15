package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PortMapper;
import org.usfirst.frc.team568.robot.commands.ArmDown;
import org.usfirst.frc.team568.robot.commands.ArmUP;
import org.usfirst.frc.team568.robot.stronghold.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

public class Arms extends SubsystemBase {

	Relay leftarm;
	Relay rightarm;
	public DigitalInput topLimmitSwitch;
	public DigitalInput bottomLimmitSwitch;

	public Arms(PortMapper ports) {
		super(ports);
		
		leftarm = new Relay(port("spike1"));
		rightarm = new Relay(port("spike2"));
		topLimmitSwitch = new DigitalInput(port("topLimmitSwitch"));
		bottomLimmitSwitch = new DigitalInput(port("bottomLimmitSwitch"));

		Robot.getInstance().oi.armsUp.whileHeld(new ArmUP());
		Robot.getInstance().oi.armsDown.whileHeld(new ArmDown());

	}

	public void goDown() {
		leftarm.set(Relay.Value.kForward);
		rightarm.set(Relay.Value.kReverse);
		System.out.println("Arm Down");
	}

	public void goUp() {
		leftarm.set(Relay.Value.kReverse);
		rightarm.set(Relay.Value.kForward);
	}

	public void stop() {
		leftarm.set(Relay.Value.kOff);
		rightarm.set(Relay.Value.kOff);
		System.out.println("stop");
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
