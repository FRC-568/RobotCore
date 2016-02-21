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

public class ReferenceFrame2 extends Subsystem implements Runnable {
	ADXRS450_Gyro gyro;
	ADXL362 acel;
	Thread updateThread;
	protected Vector2 acceleration;
	protected Vector2 velocity;
	protected Vector2 position;
	public double samples;
	protected Vector2 acelBias;

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
		samples = 250;

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
		// gyro.reset();
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

	public void calibrateAcel() {
		double avgTotalX = 0;
		double avgTotalY = 0;

		for (int i = 0; i < samples; i++) {
			avgTotalX += acel.getX() / samples;
			avgTotalY += acel.getY() / samples;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

				break;
			}
		}
		acelBias = new Vector2(-avgTotalX, -avgTotalY);

	}

	@Override
	public void run() {
		SmartDashboard.putString("Status", "Started");
		double lastTime = Timer.getFPGATimestamp();
		double time = lastTime;
		double deltaTime;

		PIDSource xSource = new PIDSource() {

			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {

			}

			@Override
			public PIDSourceType getPIDSourceType() {

				return null;
			}

			@Override
			public double pidGet() {

				return acel.getX() + acelBias.x;
			}

		};

		PIDSource ySource = new PIDSource() {

			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				// TODO Auto-generated method stub

			}

			@Override
			public PIDSourceType getPIDSourceType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public double pidGet() {
				// TODO Auto-generated method stub
				return acel.getY() + acelBias.y;
			}

		};

		Filter xFilter = LinearDigitalFilter.movingAverage(xSource, 4);
		Filter yFilter = LinearDigitalFilter.movingAverage(ySource, 4);
		SmartDashboard.putString("Status", "Filtered");

		while (!Thread.interrupted()) {
			SmartDashboard.putString("Status", "looping");
			time = Timer.getFPGATimestamp();
			SmartDashboard.putString("Status", "timestamped");
			deltaTime = time - lastTime;
			acceleration = new Vector2(xFilter.pidGet(), yFilter.pidGet());
			SmartDashboard.putString("Status", "accelerated");
			velocity = Vector2.add(velocity, Vector2.scale(acceleration, deltaTime));
			SmartDashboard.putString("Status", "velocirated");
			position = Vector2.add(position, Vector2.rotate(Vector2.scale(velocity, deltaTime), -getHeading()));
			SmartDashboard.putString("Status", "positioned");
			SmartDashboard.putNumber("Raw Y", acel.getY());
			SmartDashboard.putNumber("Raw X", acel.getX());
			lastTime = time;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				SmartDashboard.putString("Interrupted", "true");
			}
		}

	}

}