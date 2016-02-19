package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.util.Vector2;

import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.filters.Filter;
import edu.wpi.first.wpilibj.filters.LinearDigitalFilter;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ReferenceFrame2 extends Subsystem {
	public int calibrationSamples = 250;
	public int calibrationSampleRate = 20;

	protected ADXRS450_Gyro gyro;
	protected ADXL362 acel;
	protected Thread updateThread;
	protected Vector2 acceleration;
	protected Vector2 velocity;
	protected Vector2 position;
	protected Vector2 acelBias;

	private double lastTimestamp;
	private Filter xFilter;
	private Filter yFilter;

	public ReferenceFrame2() {
		acceleration = Vector2.zero;
		velocity = Vector2.zero;
		position = Vector2.zero;

		gyro = new ADXRS450_Gyro();
		acel = new ADXL362(Range.k16G);
		calibrateAcel();

		xFilter = LinearDigitalFilter.movingAverage(new PIDSource() {
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
			}

			@Override
			public PIDSourceType getPIDSourceType() {
				return PIDSourceType.kDisplacement;
			}

			@Override
			public double pidGet() {
				return acel.getX() - acelBias.x;
			}
		}, 4);

		yFilter = LinearDigitalFilter.movingAverage(new PIDSource() {
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
			}

			@Override
			public PIDSourceType getPIDSourceType() {
				return PIDSourceType.kDisplacement;
			}

			@Override
			public double pidGet() {
				return acel.getY() - acelBias.y;
			}
		}, 4);
	}

	public void start() {
		if (updateThread != null)
			updateThread.interrupt();

		updateThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				updateAcel();
				SmartDashboard.putNumber("Raw Y", acel.getY());
				SmartDashboard.putNumber("Raw X", acel.getX());
				try {
					Thread.sleep(calibrationSampleRate);
				} catch (InterruptedException e) {
					break;
				}
			}
		});
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

	}

	public void calabrateGyro() {
		gyro.calibrate();
	}

	public void calibrateAcel() {
		double avgX = 0;
		double avgY = 0;

		for (int i = 0; i < calibrationSamples; i++) {
			avgX += acel.getX() / calibrationSamples;
			avgY += acel.getY() / calibrationSamples;
			Timer.delay(calibrationSampleRate / 1000.0);
		}
		acelBias = new Vector2(avgX, avgY);
		lastTimestamp = Timer.getFPGATimestamp();
	}

	public void updateAcel() {
		double timestamp = Timer.getFPGATimestamp();
		double deltaTime = timestamp - lastTimestamp;
		acceleration = new Vector2(xFilter.pidGet(), yFilter.pidGet());
		velocity = Vector2.add(velocity, Vector2.scale(acceleration, deltaTime));
		position = Vector2.add(position, Vector2.rotate(Vector2.scale(velocity, deltaTime), -getHeading()));
		lastTimestamp = timestamp;
	}

}
