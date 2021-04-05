package frc.team568.robot.rechargemodified;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.Limelight;
import frc.team568.robot.subsystems.SubsystemBase;

public class Shooter extends SubsystemBase {

	// Motors
	private WPI_TalonFX leftShooter;
	private WPI_TalonFX rightShooter;
	private WPI_TalonFX lifter;
	private WPI_TalonFX aimer;

	// Potentiometer
	private AnalogPotentiometer pot;
	private static final double AIMER_POW = 0.4;

	// Limelight
	private Limelight limelight;

	// Servos
	private Servo hatch;
	private boolean hatchOnce = true;
	private static final double HATCH_OPEN_POS = 1;
	private static final double HATCH_CLOSE_POS = 0;

	// PID Controller
	private PIDController pidHood = new PIDController(0.001, 0.001, 0);
	private final double TOLERANCE = 1;

	// Timer
	private Timer timer = new Timer();
	private boolean pressedOnce = false;
	private double belowThresholdTime = 0;
	private double thresholdSpeed = 9; // In m/s
	private final double SHOOT_DELAY = 1.0;

	// Calculation variables
	private static final double CPR = 2048;
	private static final double METERS_PER_INCHES = 1 / 39.37;
	private static final double WHEEL_DIAMETER = 6 * METERS_PER_INCHES;
	private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
	private static final double GEAR_RATIO = 24.0 / 32.0;

	// Final variables
	private static final double SHOOT_POW = 1;
	private static final double LIFTER_POW = 0.6;
	private static final double LIFTER_HIGH = 5000;

	public Shooter(RobotBase robot) {

		super(robot);

		// Initialize motors
		leftShooter = new WPI_TalonFX(port("leftShooter"));
		rightShooter = new WPI_TalonFX(port("rightShooter"));
		lifter = new WPI_TalonFX(port("lifter"));
		aimer = new WPI_TalonFX(port("aimer"));

		// Initialize pot
		//pot = new AnalogPotentiometer(port("pot"), 60, 0);

		addChild("Left Shooter", leftShooter);
		addChild("Right Shooter", rightShooter);
		addChild("Lifter", lifter);
		addChild("Aimer", aimer);

		leftShooter.setInverted(true);
		rightShooter.setInverted(true);
		lifter.setInverted(true);
		aimer.setInverted(false);

		// Initializer servo
		hatch = new Servo(port("hatch"));
		hatch.set(HATCH_OPEN_POS);

		// Initialize PID
		pidHood.setTolerance(TOLERANCE);

		initDefaultCommand();
		
	}

	@Override
	public void periodic() {

	}

	public void initLimelight(Limelight limelight) {

		this.limelight = limelight;

	}

	public double getLeftVel() {

		return leftShooter.getSelectedSensorVelocity();

	}

	public double getRightVel() {

		return rightShooter.getSelectedSensorVelocity();

	}

	public double getLeftVelMPS() {

		return getLeftVel() * (10.0 / CPR) * WHEEL_CIRCUMFERENCE * (1 / GEAR_RATIO);

	}

	public double getRightVelMPS() {

		return getRightVel() * (10.0 / CPR) * WHEEL_CIRCUMFERENCE * (1 / GEAR_RATIO);

	}

	public int getLeftPos() {

		return leftShooter.getSelectedSensorPosition();

	}
	
	public int getRightPos() {

		return rightShooter.getSelectedSensorPosition();

	}

	public int getLifterPos() {

		return lifter.getSelectedSensorPosition();

	}

	public double getHoodPos() {

		return pot.get();

	}

	public void setHoodPos(double pos) {

		pidHood.setSetpoint(pos);
		double power = MathUtil.clamp(pidHood.calculate(getHoodPos()), -AIMER_POW, AIMER_POW);
		aimer.set(power);

	}

	private double calculateHoodPosition(double dist) {

		return 0;

	}

	protected void initDefaultCommand() {

		setDefaultCommand(new CommandBase() {

			{
				addRequirements(Shooter.this);
				SendableRegistry.addChild(Shooter.this, this);
			}

			@Override
			public void execute() {
				
				// Automatically set hood position
				//setHoodPos(calculateHoodPosition(limelight.getDistance()));

				// If the shoot button is pressed, run the motors
				if (button("shoot")) {

					// Set motor power
					leftShooter.set(SHOOT_POW);
					rightShooter.set(SHOOT_POW);
					
					// Only run if pressed once
					if (!pressedOnce) {

						pressedOnce = true;

						// Start timer the moment driver shoots
						timer.start();

					}

					// If the average MPS lower than threshold speed, continue counting
					if ((getLeftVelMPS() + getRightVelMPS()) / 2 < thresholdSpeed)
						belowThresholdTime = timer.get();
/*		
					// If time is above threshold and lifter not too high
					if (timer.get() > SHOOT_DELAY && getLifterPos() < LIFTER_HIGH) lifter.set(LIFTER_POW);
					else lifter.set(0);
*/

				} else {

					// Stop shooter
					leftShooter.set(0);
					rightShooter.set(0);

					// Also reset threshold time when not shooting
					belowThresholdTime = 0;
					timer.reset();

					// Detect if shoot button pressed once
					pressedOnce = false;
/*
					// Lower magazine when not shooting
					if (getLifterPos() > 0) lifter.set(-LIFTER_POW);
					else lifter.set(0);
*/
				}

				// Manually control lifter
				if (button("lift"))	lifter.set(LIFTER_POW);
				else if (button("lower")) lifter.set(-LIFTER_POW);
				else lifter.set(0);

				// Manually aim shooter
				if (button("extendAimer")) aimer.set(AIMER_POW);
				else if (button("retractAimer")) aimer.set(-AIMER_POW);
				else aimer.set(0);

				// Open/close hatch
				if (button("toggleHatch")) {

					if (hatchOnce) {

						hatchOnce = false;

						if (hatch.get() == HATCH_OPEN_POS) {

							hatch.set(HATCH_CLOSE_POS);

						} else {
						
							hatch.set(HATCH_OPEN_POS);

						}

					}

				} else {

					// Turn hatchOnce to true after button is not pressed
					hatchOnce = true;

				}
				
			}

		}); // End set default command

	} // End init default command

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);

		builder.addDoubleProperty("Left Shooter Power", () -> leftShooter.get(), null);
		builder.addDoubleProperty("Right Shooter Power", () -> rightShooter.get(), null);
		builder.addDoubleProperty("Left Shooter Velocity", () -> getLeftVel(), null);
		builder.addDoubleProperty("Left Shooter Position", () -> getLeftPos(), null);
		builder.addDoubleProperty("Right Shooter Velocity", () -> getRightVel(), null);
		builder.addDoubleProperty("Right Shooter Position", () -> getRightPos(), null);
		builder.addDoubleProperty("Lifter Position", () -> getLifterPos(), null);
		builder.addDoubleProperty("Left Shooter Velocity (m/s)", () -> getLeftVelMPS(), null);
		builder.addDoubleProperty("Right Shooter Velocity (m/s)", () -> getRightVelMPS(), null);
		builder.addDoubleProperty("Time Below Threshold (m/s)", () -> belowThresholdTime, null);
		builder.addDoubleProperty("Shoot Threshold Speed (m/s)", () -> thresholdSpeed, (value) -> thresholdSpeed = value);
		builder.addDoubleProperty("Hatch Position", () -> hatch.get(), null);
		builder.addDoubleProperty("Hood Position", () -> getHoodPos(), null);
		
	}

}
