package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PMW3901;
import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.util.Vector2;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.Vector2d;

public class Locator extends SubsystemBase {
	private ADXRS450_Gyro gyro;
	private PMW3901 flow;
	private Vector2d position;

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
	
	public Vector2d getPosition() {
		return position;
		
	}
	
	public void update() {
	   Vector2d motion = flow.updateMotion();
	   Vector2d rotation = rotate(motion, getAngle());
	   position.x = position.x + rotation.x;
	   position.y = position.y + rotation.y;
	   
	}
	public static Vector2d rotate(Vector2d v1, double r) {
		double rRad = Math.toRadians(r);
		double rCos = Math.cos(rRad);
		double rSin = Math.sin(rRad);
		return new Vector2d(v1.x * rCos - v1.y * rSin, v1.x * rSin + v1.y * rCos);

	}

		
}