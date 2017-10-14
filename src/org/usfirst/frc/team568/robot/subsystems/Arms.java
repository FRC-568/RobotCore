package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.StrongholdBot;
import org.usfirst.frc.team568.robot.StrongholdBotMap;
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

		leftarm = new Relay(StrongholdBotMap.spike1);
		rightarm = new Relay(StrongholdBotMap.spike2);
		topLimmitSwitch = new DigitalInput(StrongholdBotMap.topLimmitSwitch);
		bottomLimmitSwitch = new DigitalInput(StrongholdBotMap.bottomLimmitSwitch);

		StrongholdBot.getInstance().oi.armsUp.whileHeld(new ArmUP());
		StrongholdBot.getInstance().oi.armsDown.whileHeld(new ArmDown());

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
