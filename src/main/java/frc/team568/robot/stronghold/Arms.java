package frc.team568.robot.stronghold;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

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
		return new CommandBase() {
			@Override
			public void execute() {
				goDown();
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		};
	}

	public Command commandArmUp() {
		return new CommandBase() {
			@Override
			public void execute() {
				goUp();
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		};
	}

}
