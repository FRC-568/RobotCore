package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class BlockIntake extends SubsystemBase {
	public SpeedController intakeOne;
	public SpeedController intakeTwo;

	public BlockIntake(final RobotBase robot) {
		super(robot);

		intakeOne = new Spark(port("intakeOne"));
		intakeTwo = new Spark(port("intakeTwo"));
	}

}
