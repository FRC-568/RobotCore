package org.usfirst.frc.team568.robot.steamworks;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

class BlockIntake extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;

	public BlockIntake(final RobotBase robot) {
		super(robot);

		intakeOne = new Spark(port("intakeOne"));
		intakeTwo = new Spark(port("intakeTwo"));
	}

	public Command getCommandBlockIn() {
		return new Command() {
			@Override
			protected void execute() {
				intakeOne.set(1);
				intakeTwo.set(-1);
				Timer.delay(.5);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
				intakeOne.set(0);
				intakeTwo.set(0);
			}
		};
	}

	public Command getCommandBlockOut() {
		return new Command() {
			@Override
			protected void execute() {
				intakeOne.set(-1);
				intakeTwo.set(1);
				Timer.delay(.5);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
				intakeOne.set(0);
				intakeTwo.set(0);
			}
		};
	}

}
