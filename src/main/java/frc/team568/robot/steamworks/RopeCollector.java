package frc.team568.robot.steamworks;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class RopeCollector extends SubsystemBase {
	public Solenoid ropeClampIn;
	public Solenoid ropeClampOut;

	public RopeCollector(final RobotBase robot) {
		super(robot);
		
		ropeClampIn = new Solenoid(port("ropeClampIn"));
		ropeClampOut = new Solenoid(port("ropeClampOut"));

		Robot.getInstance().oi.openRopeClamp.whenPressed(this.openCommand());
		Robot.getInstance().oi.closeRopeClamp.whenPressed(this.closeCommand());
	}

	public void open() {
		ropeClampIn.set(true);
		ropeClampOut.set(false);
	}

	public void close() {
		ropeClampIn.set(false);
		ropeClampOut.set(true);
	}

	public Command openCommand() {
		return new InstantCommand(this::open);
	}

	public Command closeCommand() {
		return new InstantCommand(this::close);
	}

}
