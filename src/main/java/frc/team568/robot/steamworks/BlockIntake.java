package frc.team568.robot.steamworks;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

class BlockIntake extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;

	public BlockIntake(final RobotBase robot) {
		super(robot);

		intakeOne = new Spark(port("intakeOne"));
		intakeTwo = new Spark(port("intakeTwo"));
	}

	public Command getCommandBlockIn() {
		return new CommandBase() {
			@Override
			public void execute() {
				intakeOne.set(1);
				intakeTwo.set(-1);
				Timer.delay(.5);
			}

			@Override
			public void end(boolean interrupted) {
				intakeOne.set(0);
				intakeTwo.set(0);
			}
		};
	}

	public Command getCommandBlockOut() {
		return new CommandBase() {
			@Override
			public void execute() {
				intakeOne.set(-1);
				intakeTwo.set(1);
				Timer.delay(.5);
			}

			@Override
			public void end(boolean interrupted) {
				intakeOne.set(0);
				intakeTwo.set(0);
			}
		};
	}

}
