package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class BlockHandler extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;

	/*
	 * public Solenoid extensionO; public Solenoid extensionI; public Solenoid
	 * grabberO; public Solenoid grabberI;
	 */
	public BlockHandler(final RobotBase robot) {
		super(robot);
		/*
		 * extensionO = new Solenoid(port("extensionO")); extensionI = new
		 * Solenoid(port("extensionI")); grabberO = new Solenoid(port("grabberO"));
		 * grabberI = new Solenoid(port("grabberI"));
		 */
		intakeOne = new WPI_TalonSRX(port("intakeOne"));
		intakeTwo = new WPI_TalonSRX(port("intakeTwo"));

		intakeOne.setInverted(true);

	}

	/*
	 * public void armOut() { extensionI.set(false); extensionO.set(true); }
	 *
	 * public void armIn() { extensionO.set(false); extensionI.set(true); }
	 */
	public void blockLiftIn() {
		intakeOne.set(.5);
		intakeTwo.set(.5);
	}

	public void blockLiftOut() {
		intakeOne.set(-.75);
		intakeTwo.set(-.75);
	}

	/*
	 * public void blockGrab() { grabberO.set(false); grabberI.set(true); }
	 *
	 * public void blockRelease() { grabberI.set(false); grabberO.set(true); }
	 */
	public void allStop() {
		intakeOne.set(0);
		intakeTwo.set(0);

	}

	/*
	 * public Command getCommandArmIn() { return new Command() {
	 *
	 * @Override protected void execute() { armIn(); Timer.delay(.1); }
	 *
	 * @Override protected boolean isFinished() { return true; }
	 *
	 * @Override protected void end() {
	 *
	 * } }; }
	 *
	 * public Command getCommandArmOut() { return new Command() {
	 *
	 * @Override protected void execute() { armOut(); Timer.delay(.1); }
	 *
	 * @Override protected boolean isFinished() { return true; }
	 *
	 * @Override protected void end() {
	 *
	 * } }; }
	 */
	public Command getCommandBlockLiftIn() {
		return new Command() {
			@Override
			protected void execute() {
				blockLiftIn();
				// blockGrab();
				Timer.delay(.1);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
				allStop();
			}
		};
	}

	public Command getCommandBlockLiftIn(final double seconds) {
		return new TimedCommand(seconds) {
			@Override
			protected void execute() {
				blockLiftIn();
				// blockGrab();
			}

			@Override
			protected void end() {
				allStop();
			}
		};
	}

	public Command getCommandBlockLiftOut() {
		return new Command() {
			@Override
			protected void execute() {
				blockLiftOut();
				Timer.delay(.5);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
				allStop();
			}
		};
	}

	public Command getCommandBlockLiftOut(final double seconds) {
		return new TimedCommand(seconds) {
			@Override
			protected void execute() {
				blockLiftOut();
				Timer.delay(.5);
			}

			@Override
			protected void end() {
				allStop();
			}
		};
	}

	public Command getCommandBlockLiftOut2() {
		return new Command() {
			@Override
			protected void execute() {
				blockLiftOut();
				Timer.delay(.5);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
				allStop();
			}
		};
	}
	/*
	 * public Command blockGrabCommand() { return new Command() {
	 *
	 * @Override protected void execute() { blockGrab(); Timer.delay(.5); }
	 *
	 * @Override protected boolean isFinished() { return false; }
	 *
	 * @Override protected void end() { blockRelease(); } };
	 */
}
