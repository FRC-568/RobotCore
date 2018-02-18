package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class BlockHandler extends SubsystemBase {
	public WPI_TalonSRX intakeOne;
	public WPI_TalonSRX intakeTwo;
	public WPI_TalonSRX intakeArmL;
	public WPI_TalonSRX intakeArmR;
	public WPI_TalonSRX armMotorL;
	public WPI_TalonSRX armMotorR;

	public BlockHandler(final RobotBase robot) {
		super(robot);

		intakeOne = new WPI_TalonSRX(port("intakeOne"));
		intakeTwo = new WPI_TalonSRX(port("intakeTwo"));
		intakeArmL = new WPI_TalonSRX(port("intakeArmL"));
		intakeArmR = new WPI_TalonSRX(port("intakeArmR"));
		armMotorL = new WPI_TalonSRX(port("armMotorL"));
		armMotorR = new WPI_TalonSRX(port("armMotorR"));

		intakeOne.setInverted(true);
		intakeArmL.setInverted(true);
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
		armMotorL.set(.5);
		armMotorR.set(.5);
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
