package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PMW3901;
import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.util.Vector2;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class Locator extends SubsystemBase {
	private ADXRS450_Gyro gyro;
	private PMW3901 flow;
	private Vector2 position;

	public Locator(RobotBase source) {
		super(source);
		gyro = new ADXRS450_Gyro();
	}

	public void calibrate() {
		gyro.calibrate();
	}

	public void reset() {
		gyro.reset();
	}

	public double getAngle() {
		return gyro.getAngle();
	}

	public double getRate() {
		return gyro.getRate();
	}

	public Vector2 getPosition() {
		return position;
	}

	public void update() {
		Vector2 motion = flow.updateMotion();
		position = motion.rotate(getAngle()).add(position);
	}

}