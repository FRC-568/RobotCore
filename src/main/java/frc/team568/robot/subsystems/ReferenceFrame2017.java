package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;
import frc.team568.util.Vector2;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.filters.Filter;
import edu.wpi.first.wpilibj.filters.LinearDigitalFilter;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ReferenceFrame2017 extends SubsystemBase implements Gyro {
	public int calibrationSamples = 250;
	public int calibrationSampleRate = 20;

	protected ADXRS450_Gyro gyro;
	protected BuiltInAccelerometer acel;
	protected Thread updateThread;
	protected Vector2 acceleration;
	protected Vector2 velocity;
	protected Vector2 position;
	protected Vector2 acelBias;
	public Encoder motorEncoder;
	private double lastTimestamp;
	private Filter xFilter;
	private Filter yFilter;
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
		acel = new BuiltInAccelerometer(Range.k8G);
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
				double x = acel.getX() - acelBias.x;
				if (x > threshold || x < -threshold)
					return x;
				else
					return 0;
			}
		}, filterPoles);

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
				double y = acel.getY() - acelBias.y;
				if (y > threshold || y < -threshold)
					return y;
				else
					return 0;
			}
		}, filterPoles);
	}

	public void start() {
		if (updateThread != null)
			updateThread.interrupt();

		updateThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				updateAcel();
				// SmartDashboard.putNumber("Raw Y", acel.getY() - acelBias.y);
				// SmartDashboard.putNumber("Raw X", acel.getX() - acelBias.x);
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

	// int currentTicks = motorEncoder.getRaw();
	public double DistanceTraveled() {
		return motorEncoder.getDistance();
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
			avgX += acel.getX();
			avgY += acel.getY();
			Timer.delay(0.005);// calibrationSampleRate / 1000.0);
		}
		avgX /= calibrationSamples;
		avgY /= calibrationSamples;
		acelBias = Vector2.of(avgX, avgY);
		lastTimestamp = Timer.getFPGATimestamp();
		// SmartDashboard.putNumber("X Bias", acelBias.x);
		// SmartDashboard.putNumber("Y Bias", acelBias.y);
	}

	public void run() {
		SmartDashboard.putString("Status", "Started");
		double lastTime = Timer.getFPGATimestamp();
		double time = lastTime;
		double deltaTime;

		while (!Thread.interrupted()) {
			time = Timer.getFPGATimestamp();
			deltaTime = time - lastTime;
			acceleration = Vector2.of(xFilter.pidGet(), yFilter.pidGet());
			velocity = acceleration.scale(deltaTime).add(velocity);
			position = velocity.scale(deltaTime).rotate(-getHeading()).add(position);

			lastTime = time;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void updateAcel() {
		double timestamp = Timer.getFPGATimestamp();
		double deltaTime = timestamp - lastTimestamp;
		acceleration = Vector2.of(xFilter.pidGet(), yFilter.pidGet());
		velocity = acceleration.scale(deltaTime).rotate(-getHeading()).add(velocity);
		position = velocity.scale(deltaTime).add(position);
		lastTimestamp = timestamp;
	}

	@Override
	public void calibrate() {
		gyro.calibrate();
	}

	@Override
	public double getRate() {
		return gyro.getRate();
	}

}
