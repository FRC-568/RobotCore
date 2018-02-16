package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

public class BlockIntake extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;

	public BlockIntake(final RobotBase robot) {
		super(robot);

		intakeOne = new WPI_TalonSRX(port("intakeOne"));
		intakeTwo = new WPI_TalonSRX(port("intakeTwo"));

		intakeOne.setInverted(true);
	}

}
