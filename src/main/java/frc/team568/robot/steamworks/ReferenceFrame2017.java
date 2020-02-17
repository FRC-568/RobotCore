package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import frc.team568.util.Vector2;

public class ReferenceFrame2017 extends SubsystemBase implements Gyro {
	public int calibrationSamples = 250;
	public int calibrationSampleRate = 20;

	protected ADXRS450_Gyro gyro;
	protected BuiltInAccelerometer accel;
	protected Thread updateThread;
	protected Vector2 acceleration;
	protected Vector2 velocity;
	protected Vector2 position;
	protected Vector2 acelBias;
	public Encoder motorEncoder;
	private double lastTimestamp;
	private LinearFilter xFilter;
	private LinearFilter yFilter;
	public double threshold;
	public double centimetersTraveled;
	private static final int filterPoles = 20;

	double ticksPerRotation = 360;
	double wheelDiameterInCM = 15;

	public ReferenceFrame2017(final RobotBase robot) {
		super(robot);

		acceleration = Vector2.zero;
		velocity = Vector2.zero;
		position = Vector2.zero;
		threshold = .03;

		motorEncoder = new Encoder(port("encoderYellow"), port("encoderWhite"), false, EncodingType.k4X);
		motorEncoder.setDistancePerPulse(.01);

		gyro = new ADXRS450_Gyro();
		accel = new BuiltInAccelerometer(Range.k8G);
		calibrateAccel();

		xFilter = LinearFilter.movingAverage(filterPoles);
		yFilter = LinearFilter.movingAverage(filterPoles);
	}

	public void start() {
		if (updateThread != null)
			updateThread.interrupt();

		updateThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				updateAccel();
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

	@Override
	public void reset() {
		acceleration = Vector2.zero;
		velocity = Vector2.zero;
		position = Vector2.zero;
		gyro.reset();
	}

	public double DistanceTraveled() {
		return motorEncoder.getDistance();
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public double getHeading() {
		double clip = -(gyro.getAngle()) % 360;
		return (clip < 0) ? 360 + clip : clip;
	}

	@Override
	public double getAngle() {
		return gyro.getAngle();
	}

	public Vector2 getAcel() {
		return acceleration;
	}

	public Vector2 getPos() {
		return position;
	}

	public void calibrateGyro() {
		gyro.calibrate();
	}

	public void calibrateAccel() {
		double avgX = 0;
		double avgY = 0;

		for (int i = 0; i < calibrationSamples; i++) {
			avgX += accel.getX();
			avgY += accel.getY();
			Timer.delay(0.005);// calibrationSampleRate / 1000.0);
		}
		avgX /= calibrationSamples;
		avgY /= calibrationSamples;
		acelBias = Vector2.of(avgX, avgY);
		lastTimestamp = Timer.getFPGATimestamp();
	}

	public void updateAccel() {
		double timestamp = Timer.getFPGATimestamp();
		double deltaTime = timestamp - lastTimestamp;
		acceleration = Vector2.of(xFilter.calculate(getRawX()), yFilter.calculate(getRawY()));
		velocity = acceleration.scale(deltaTime).rotate(-getHeading()).add(velocity);
		position = velocity.scale(deltaTime).add(position);
		lastTimestamp = timestamp;
	}

	protected double getRawX() {
		double x = accel.getX() - acelBias.x;
		return (x > threshold || x < -threshold) ? x : 0;
	}

	protected double getRawY() {
		double y = accel.getY() - acelBias.y;
		return (y > threshold || y < -threshold) ? y : 0;
	}

	@Override
	public void calibrate() {
		calibrateGyro();
		calibrateAccel();
	}

	@Override
	public double getRate() {
		return gyro.getRate();
	}

	@Override
	public void close() throws Exception {
		stop();
		gyro.close();
		accel.close();
	}

}
