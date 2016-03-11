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

		leftarm = new Relay(RobotMap.Spike1);
		rightarm = new Relay(RobotMap.Spike2);
		topLimmitSwitch = new DigitalInput(RobotMap.topLimmitSwitch);
		bottomLimmitSwitch = new DigitalInput(RobotMap.bottomLimmitSwitch);

		Robot.getInstance().oi.shootSix.whileHeld(new ArmUP());
		Robot.getInstance().oi.shootSeven.whileHeld(new ArmDown());

	}

	public void GoDown() {
		leftarm.set(Relay.Value.kReverse);
		rightarm.set(Relay.Value.kForward);
	}

	public void GoUp() {
		leftarm.set(Relay.Value.kForward);
		rightarm.set(Relay.Value.kReverse);
	}

	public void Stop() {
		leftarm.set(Relay.Value.kOff);
		rightarm.set(Relay.Value.kOff);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
