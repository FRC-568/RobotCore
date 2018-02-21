package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class BlockHandler extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;
	public SpeedController intakeArmL;
	public SpeedController intakeArmR;
	public SpeedController armMotorL;
	public SpeedController armMotorR;

	public BlockHandler(final RobotBase robot) {
		super(robot);

		intakeOne = new WPI_TalonSRX(port("intakeOne"));
		intakeTwo = new WPI_TalonSRX(port("intakeTwo"));
		intakeArmL = new Spark(port("intakeArmL"));
		intakeArmR = new Spark(port("intakeArmR"));
		armMotorL = new Talon(port("armMotorL"));
		armMotorR = new Talon(port("armMotorR"));

		intakeOne.setInverted(true);
		intakeArmR.setInverted(true);
		intakeArmL.setInverted(false);
		armMotorR.setInverted(true);
	}

	public void armOut() {
		intakeArmL.set(.75);
		intakeArmR.set(.75);
	}

	public void blockSpinL() {
		armMotorL.set(.75);
		armMotorR.set(-.75);
	}

	public void blockSpinR() {
		armMotorL.set(-.75);
		armMotorR.set(.75);
	}


	public void armIn() {
		intakeArmL.set(-.75);
		intakeArmR.set(-.75);
	}

	public void blockLiftIn() {
		intakeOne.set(.5);
		intakeTwo.set(.5);
	}

	public void blockLiftOut() {
		intakeOne.set(-1);
		intakeTwo.set(-1);
	}

	public void blockArmIn() {
		armMotorL.set(1);
		armMotorR.set(1);
	}

	public void blockArmOut() {
		armMotorL.set(1);
		armMotorR.set(1);
	}

	public void allStop() {
		intakeArmL.set(0);
		intakeArmR.set(0);
		intakeOne.set(0);
		intakeTwo.set(0);
		armMotorL.set(0);
		armMotorR.set(0);
	}

	public Command getCommandArmIn() {
		return new Command() {
			@Override
			protected void execute() {
				armIn();
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

	public Command getCommandArmOut() {
		return new Command() {
			@Override
			protected void execute() {
				armOut();
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

	public Command getCommandBlockLiftIn() {
		return new Command() {
			@Override
			protected void execute() {
				blockLiftIn();
				blockArmIn();
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

	public Command getCommandBlockLiftOut2() {
		return new Command() {
			@Override
			protected void execute() {
				blockArmOut();
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
}
