package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.simulation.AnalogGyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.system.plant.DCMotor;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.VecBuilder;
import frc.team568.robot.RobotBase;

public class TwoMotorDrive extends SubsystemBase {
	
	protected PowerDistributionPanel pdp;
	private Field2d m_field = new Field2d();

	// These represent our regular encoder objects, which we would create to use on a real robot.
	private Encoder m_leftEncoder = new Encoder(0, 1);
	private Encoder m_rightEncoder = new Encoder(2, 3);

	// These are our EncoderSim objects, which we will only use in
	// simulation. However, you do not need to comment out these
	// declarations when you are deploying code to the roboRIO.
	private EncoderSim m_leftEncoderSim = new EncoderSim(m_leftEncoder);
	private EncoderSim m_rightEncoderSim = new EncoderSim(m_rightEncoder);

	protected WPI_TalonSRX leftMotor;
	protected WPI_TalonSRX rightMotor;
	protected Pose2d m_pose;
	protected Gyro gyro = new ADXRS450_Gyro();

	// Create our gyro object like we would on a real robot.
	private AnalogGyro m_gyro = new AnalogGyro(1);

	// Create the simulated gyro object, used for setting the gyro
	// angle. Like EncoderSim, this does not need to be commented out
	// when deploying code to the roboRIO.
	private AnalogGyroSim m_gyroSim = new AnalogGyroSim(m_gyro);

	DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(
  	m_gyro.getRotation2d(), new Pose2d(5.0, 13.5, new Rotation2d()));

	// Create the simulation model of our drivetrain.
	DifferentialDrivetrainSim m_driveSim = new DifferentialDrivetrainSim(
		DCMotor.getNEO(2),       // 2 NEO motors on each side of the drivetrain.
		7.29,                    // 7.29:1 gearing reduction.
		7.5,                     // MOI of 7.5 kg m^2 (from CAD model).
		60.0,                    // The mass of the robot is 60 kg.
		Units.inchesToMeters(3), // The robot uses 3" radius wheels.
		0.7112,                  // The track width is 0.7112 meters.
	
		// The standard deviations for measurement noise:
		// x and y:          0.001 m
		// heading:          0.001 rad
		// l and r velocity: 0.1   m/s
		// l and r position: 0.005 m
		VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005)
	);

	protected double maxLeftCurrent = 0;
	protected double maxRightCurrent = 0;

	private boolean override = false;

	protected boolean safeMode = false;

	public TwoMotorDrive(RobotBase robot) {

		super(robot);

		SmartDashboard.putData("Field", m_field);
		m_leftEncoder.setDistancePerPulse(2 * Math.PI * 6.0 / 3000.0);
 		m_rightEncoder.setDistancePerPulse(2 * Math.PI * 6.0 / 3000.0);

		// Initialize code
		initMotors();

		pdp = new PowerDistributionPanel();
		
	}

	@Override
	public void simulationPeriodic() {
		// Set the inputs to the system. Note that we need to convert
		// the [-1, 1] PWM signal to voltage by multiplying it by the
		// robot controller voltage.
		m_driveSim.setInputs(leftMotor.get() * RobotController.getInputVoltage(),
							 rightMotor.get() * RobotController.getInputVoltage());
	  
		// Advance the model by 20 ms. Note that if you are running this
		// subsystem in a separate thread or have changed the nominal timestep
		// of TimedRobot, this value needs to match it.
		m_driveSim.update(0.02);
	  
		// Update all of our sensors.
		m_leftEncoderSim.setDistance(m_driveSim.getLeftPositionMeters());
		m_leftEncoderSim.setRate(m_driveSim.getLeftVelocityMetersPerSecond());
		m_rightEncoderSim.setDistance(m_driveSim.getRightPositionMeters());
		m_rightEncoderSim.setRate(m_driveSim.getRightVelocityMetersPerSecond());
		m_gyroSim.setAngle(-m_driveSim.getHeading().getDegrees());
	  }

	private void initMotors() {
		
		leftMotor = new WPI_TalonSRX(port("leftMotor"));
		rightMotor = new WPI_TalonSRX(port("rightMotor"));

		addChild("Left Motor", leftMotor);
		addChild("Right Motor", rightMotor);

		leftMotor.setInverted(false);
		rightMotor.setInverted(true);
		
	}

	@Override
	public void periodic() {
		
		var gyroAngle = Rotation2d.fromDegrees(-m_gyro.getAngle());

		// Update the pose
		m_pose = m_odometry.update(gyroAngle, m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
		m_field.setRobotPose(m_odometry.getPoseMeters());

	}

	public void resetGyro() {

		gyro.reset();

	}

	public double getAngle() {

		return gyro.getAngle();

	}

	public void resetMotors() {

		leftMotor.setSelectedSensorPosition(0);
		rightMotor.setSelectedSensorPosition(0);

	}

	public void stop() {

		setLeft(0);
		setRight(0);

	}

	public void override() {

		override = true;

	}

	public void stopOverride() {

		override = false;

	}

	public void setLeft(double val) {

		leftMotor.set(val);

	}

	public void setRight(double val) {

		rightMotor.set(val);

	}

	public double getLeftVel() {

		return leftMotor.getSelectedSensorVelocity();

	}

	public double getRightVel() {

		return rightMotor.getSelectedSensorVelocity();

	}

	public double getLeftPos() {

		return leftMotor.getSelectedSensorPosition();

	}
	
	public double getRightPos() {

		return rightMotor.getSelectedSensorPosition();

	}

	public double getLeftCurrent() {

		return pdp.getCurrent(port("leftMotor"));

	}

	public double getRightCurrent() {

		return pdp.getCurrent(port("rightMotor"));

	}

	public void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {

			double comboStartTime = 0;
			boolean alreadyToggled = false;

			{
				addRequirements(TwoMotorDrive.this);
				SendableRegistry.addChild(TwoMotorDrive.this, this);
			}

			@Override
			public void execute() {

				if (button("safeModeToggle")) {

					if (comboStartTime == 0)
						comboStartTime = Timer.getFPGATimestamp();
					else if (Timer.getFPGATimestamp() - comboStartTime >= 3.0 && !alreadyToggled) {
					
						safeMode = !safeMode;
						alreadyToggled = true;
						System.out.println("Safemode is " + (safeMode ? "Enabled" : "Disabled") + ".");
					
					}

				} else {

					comboStartTime = 0;
					alreadyToggled = false;
				
				}
				
				// Slower drive if safe mode
				double driveDamp = 1;
				if (safeMode)
					driveDamp = 0.5;

				// Arcade Drive
				double leftPower = axis("forward") * 0.5 - axis("turn") * 0.3;
				double rightPower = axis("forward") * 0.5 + axis("turn") * 0.3;
				double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
				if (max > 1.0) {

					leftPower /= max;
					rightPower /= max;

				}

				// Set motor powers
				if (!override) {

					leftMotor.set(leftPower * driveDamp * 0.7);
					rightMotor.set(rightPower * driveDamp * 0.7);

				}

				// Set the maximum current
				if (maxLeftCurrent < getLeftCurrent()) maxLeftCurrent = getLeftCurrent();
				if (maxRightCurrent < getRightCurrent()) maxRightCurrent = getRightCurrent();

			}

		}); // End set default command

	} // End init default command

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);

		builder.addDoubleProperty("Forward", () -> axis("forward"), null);
		builder.addDoubleProperty("Side", () -> axis("side"), null);
		builder.addDoubleProperty("Turn", () -> axis("turn"), null);
		builder.addDoubleProperty("Angle", () -> getAngle(), null);
		builder.addDoubleProperty("Left Motor Power", () -> leftMotor.get(), null);
		builder.addDoubleProperty("Right Motor Power", () -> rightMotor.get(), null);
		builder.addDoubleProperty("Left Motor Velocity", () -> getLeftVel(), null);
		builder.addDoubleProperty("Left Motor Position", () -> getLeftPos(), null);
		builder.addDoubleProperty("Right Motor Velocity", () -> getRightVel(), null);
		builder.addDoubleProperty("Right Motor Position", () -> getRightPos(), null);
		builder.addDoubleProperty("Left Motor Current", () -> getLeftCurrent(), null);
		builder.addDoubleProperty("Right Motor Current", () -> getRightCurrent(), null);
		builder.addDoubleProperty("Maximum Left Motor Current", () -> maxLeftCurrent, null);
		builder.addDoubleProperty("Maximum Right Motor Current", () -> maxRightCurrent, null);
		
	}

}
