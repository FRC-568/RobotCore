package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.stronghold.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;

public class Arms extends SubsystemBase {
	Relay leftarm;
	Relay rightarm;
	public DigitalInput topLimmitSwitch;
	public DigitalInput bottomLimmitSwitch;

	public Arms(final RobotBase robot) {
		super(robot);
		
		leftarm = new Relay(port("spike1"));
		rightarm = new Relay(port("spike2"));
		topLimmitSwitch = new DigitalInput(port("topLimmitSwitch"));
		bottomLimmitSwitch = new DigitalInput(port("bottomLimmitSwitch"));

		Robot.getInstance().oi.armsUp.whileHeld(commandArmUp());
		Robot.getInstance().oi.armsDown.whileHeld(commandArmDown());
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
	
	public Command commandArmDown() {
		return new Command() {
			@Override
			protected void execute() {
				goDown();
			}

			@Override
			protected boolean isFinished() {
				return !Robot.getInstance().oi.armsDown.get();
			}

			@Override
			protected void end() {
				stop();
			}
		};
	}
	
	public Command commandArmUp() {
		return new Command() {
			@Override
			protected void execute() {
				goUp();
			}

			@Override
			protected boolean isFinished() {
				return !Robot.getInstance().oi.armsUp.get();
			}

			@Override
			protected void end() {
				Robot.getInstance().arms.stop();
			}
		};
	}

}
