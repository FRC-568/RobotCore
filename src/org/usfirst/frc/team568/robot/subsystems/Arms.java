package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.RobotMap;
import org.usfirst.frc.team568.robot.commands.ArmDown;
import org.usfirst.frc.team568.robot.commands.ArmUP;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arms extends Subsystem {

	Relay leftarm;
	Relay rightarm;
	public DigitalInput topLimmitSwitch;
	public DigitalInput bottomLimmitSwitch;

	public Arms() {

		leftarm = new Relay(RobotMap.spike1);
		rightarm = new Relay(RobotMap.spike2);
		topLimmitSwitch = new DigitalInput(RobotMap.topLimmitSwitch);
		bottomLimmitSwitch = new DigitalInput(RobotMap.bottomLimmitSwitch);

		Robot.getInstance().oi.armsSix.whenPressed(new ArmUP());
		Robot.getInstance().oi.armsSeven.whenPressed(new ArmDown());

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
