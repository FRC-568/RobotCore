package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.RobotBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

public class BlockLift2018 extends SubsystemBase {
	public SpeedController lift_m;

	public BlockLift2018(final RobotBase robot) {
		super(robot);

		lift_m = new WPI_TalonSRX(port("lift"));
	}

}
