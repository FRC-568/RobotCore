package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.util.Vector2;

import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class ReferenceFrame2 extends Subsystem implements Runnable {
	ADXRS450_Gyro gyro;
	ADXL362 acel;
	Thread updateThread;
	protected Vector2 acceleration;
	protected Vector2 velocity;
	protected Vector2 position;

	// double currentPosition = imu.getDisX();
	double startingPosition = 0;
	// double currentHeading = imu.getAngleZ();
	/// double wantedHeading = currentHeading;
	static double tolerance = 4;

	public ReferenceFrame2() {
		gyro = new ADXRS450_Gyro();
		acel = new ADXL362(Range.k16G);
		acceleration = Vector2.zero;
		velocity = Vector2.zero;
		position = Vector2.zero;

	}

	public void start() {
		if (updateThread != null) {
			updateThread.interrupt();
		}
		updateThread = new Thread(this);
		updateThread.start();

	}

	public void stop() {
		updateThread.interrupt();
	}

	public void reset() {
		acceleration = Vector2.zero;
		velocity = Vector2.zero;
		position = Vector2.zero;
		gyro.reset();
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public double getHeading() {
		double unclipped = -(gyro.getAngle());
		double clip = unclipped % 360;
		if (clip < 0)
			return 360 + clip;
		else
			return clip;
	}

	public Vector2 getAcel() {
		return acceleration;

	}

	public Vector2 getPos() {
		return position;
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}

	public void calabrateGyro() {
		gyro.calibrate();
	}

	@Override
	public void run() {
		double lastTime = Timer.getFPGATimestamp();
		double time = lastTime;
		double deltaTime;
		while (!Thread.interrupted()) {
			time = Timer.getFPGATimestamp();
			deltaTime = time - lastTime;
			acceleration = new Vector2(acel.getX(), acel.getY());
			velocity = Vector2.add(velocity, Vector2.scale(acceleration, deltaTime));
			position = Vector2.add(position, Vector2.rotate(Vector2.scale(velocity, deltaTime), -getHeading()));

			lastTime = time;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}
		}

	}

}