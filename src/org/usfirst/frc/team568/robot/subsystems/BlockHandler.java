package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

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
		armMotorL.setInverted(true);
	}

	public void armOut() {
		intakeArmL.set(.25);
		intakeArmR.set(.25);
	}

	public void armIn() {
		intakeArmL.set(-.25);
		intakeArmR.set(-.25);
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
		armMotorL.set(.75);
		armMotorR.set(.75);
	}

	public void blockArmOut() {
		armMotorL.set(-.5);
		armMotorR.set(-.5);
	}

	public void allStop() {
		intakeArmL.set(0);
		intakeArmR.set(0);
		intakeOne.set(0);
		intakeTwo.set(0);
		armMotorL.set(0);
		armMotorR.set(0);
	}
}
