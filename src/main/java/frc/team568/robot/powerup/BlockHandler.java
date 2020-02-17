package frc.team568.robot.powerup;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class BlockHandler extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;

	public Solenoid extensionO;
	public Solenoid extensionI;
	public Solenoid grabberO;
	public Solenoid grabberI;

	public BlockHandler(final RobotBase robot) {
		super(robot);

		extensionO = new Solenoid(port("extensionO"));
		extensionI = new Solenoid(port("extensionI"));
		grabberO = new Solenoid(port("grabberO"));
		grabberI = new Solenoid(port("grabberI"));

		intakeOne = new WPI_TalonSRX(port("intakeOne"));
		intakeTwo = new WPI_TalonSRX(port("intakeTwo"));

		intakeOne.setInverted(true);
	}

	public void armOut() {
		extensionI.set(false);
		extensionO.set(true);
	}

	public void armIn() {
		extensionO.set(false);
		extensionI.set(true);
	}

	public void blockLiftIn() {
		intakeOne.set(.5);
		intakeTwo.set(.5);
	}

	public void blockLiftOut() {
		intakeOne.set(-.75);
		intakeTwo.set(-.75);
	}

	public void blockGrab() {
		grabberO.set(false);
		grabberI.set(true);
	}

	public void blockRelease() {
		grabberI.set(false);
		grabberO.set(true);
	}

	public void allStop() {
		intakeOne.set(0);
		intakeTwo.set(0);
	}

	public Command getCommandArmIn() {
		return new InstantCommand(this::armIn);
	}

	public Command getCommandArmOut() {
		return new InstantCommand(this::armOut);
	}

	public Command getCommandBlockLiftIn() {
		return new CommandBase() {
			@Override
			public void execute() {
				blockLiftIn();
				// blockGrab();
			}

			@Override
			public void end(boolean interrupted) {
				allStop();
			}
		};
	}

	public Command getCommandBlockLiftIn(final double seconds) {
		return new CommandBase() {
			@Override
			public void execute() {
				blockLiftIn();
				// blockGrab();
			}

			@Override
			public void end(boolean interrupted) {
				allStop();
			}
		}.withTimeout(seconds);
	}

	public Command getCommandBlockLiftOut() {
		return new CommandBase() {
			@Override
			public void execute() {
				blockLiftOut();
			}

			@Override
			public void end(boolean interrupted) {
				allStop();
			}
		};
	}

	public Command getCommandBlockLiftOut(final double seconds) {
		return new CommandBase() {
			@Override
			public void execute() {
				blockLiftOut();
			}

			@Override
			public void end(boolean interrupted) {
				allStop();
			}
		}.withTimeout(seconds);
	}

	public Command getCommandBlockLiftOut2() {
		return new CommandBase() {
			@Override
			public void execute() {
				blockLiftOut();
			}

			@Override
			public void end(boolean interrupted) {
				allStop();
			}
		};
	}

	public Command blockGrabCommand() {
		return new CommandBase() {
			@Override
			public void execute() {
				blockGrab();
			}

			@Override
			public void end(boolean interrupted) {
				blockRelease();
			}
		};
	}
}
