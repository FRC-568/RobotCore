package frc.team568.robot.rechargemodified;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
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
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Shooter extends SubsystemBase {

	// Motors
	private WPI_TalonFX leftShooter;
	private WPI_TalonFX rightShooter;
	private WPI_TalonFX lifter;
	private WPI_TalonFX aimer;
	private TwoMotorDrive drive;
	private final double MIN_POWER = 0.05;
	private final double TURN_P = 0.1;

	private enum MODES { green, yellow, def };
	private MODES hoodMode = MODES.def;
	private final int GREEN_POS = 559;
	private final int YELLOW_POS = 616;
	private final int BLUE_POS = 619;
	private final int RED_POS = 620;

	// Sensors
	private AnalogInput pot;
	private static final double AIMER_POW = 0.4;
	private static final int DEFAULT_AIMER = 620;
	private DigitalInput limit;
	private List<Integer> potPos = new ArrayList<Integer>();

	// Limelight
	private Limelight limelight;

	// Servos
	private Servo hatch;
	private boolean hatchOnce = true;
	private static final double HATCH_OPEN_POS = 1;
	private static final double HATCH_CLOSE_POS = 0;

	// PID Controller
	private PIDController pidHood = new PIDController(0.006, 0.0001, 0);
	private final double TOLERANCE = 2;

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
	private static final double LIFTER_POW = 0.4;
	private static final int LIFTER_HIGH = -17500;

	public Shooter(RobotBase robot) {

		super(robot);

		// Initialize motors
		leftShooter = new WPI_TalonFX(port("leftShooter"));
		rightShooter = new WPI_TalonFX(port("rightShooter"));
		lifter = new WPI_TalonFX(port("lifter"));
		aimer = new WPI_TalonFX(port("aimer"));

		// Initialize pot
		pot = new AnalogInput(port("pot"));
		pot.setOversampleBits(4);
		pot.setAverageBits(4);

		// Initialize limit switch
		limit = new DigitalInput(port("limit"));

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

	public void getDrive(TwoMotorDrive drive) {

		this.drive = drive;
		
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

	public double getLeftPos() {

		return leftShooter.getSelectedSensorPosition();

	}
	
	public double getRightPos() {

		return rightShooter.getSelectedSensorPosition();

	}

	public double getLifterPos() {

		return lifter.getSelectedSensorPosition();

	}

	public double getHoodPos() {

		return potPos.stream().mapToInt(val -> val).average().orElse(0.0);

	}

	public void setHoodPos(double pos) {

		pidHood.setSetpoint(pos);
		double power = MathUtil.clamp(pidHood.calculate(getHoodPos()), -AIMER_POW, AIMER_POW);
		aimer.set(power);

	}

	private int calculateHoodPosition(double dist) {

		if (limelight.getDistance() >= 150 && limelight.getDistance() < 210) {

			return BLUE_POS;

		} else if (limelight.getDistance() >= 210 && limelight.getDistance() <= 270) {

			return RED_POS;

		} else return DEFAULT_AIMER;

	}

	protected void initDefaultCommand() {

		setDefaultCommand(new CommandBase() {

			{
				addRequirements(Shooter.this);
				SendableRegistry.addChild(Shooter.this, this);
			}

			@Override
			public void execute() {

				if (button("green")) hoodMode = MODES.green;
				if (button("yellow")) hoodMode = MODES.yellow;
				if (button("def")) hoodMode = MODES.def;

				if (pot.getAverageValue() > 500) potPos.add(pot.getAverageValue());
				if (potPos.size() > 100) potPos.remove(0);

				// Set encoder position using limit switch
				if (!limit.get()) lifter.setSelectedSensorPosition(LIFTER_HIGH);

				// Automatically set hood position
				if (hoodMode == MODES.def) {

					// Manually aim shooter
					if (button("extendAimer")) {
					
						aimer.set(AIMER_POW);
					
					}
					else if (button("retractAimer")) {
					
						aimer.set(-AIMER_POW);

					}
					else aimer.set(0);

				} else if (hoodMode == MODES.green) {

					setHoodPos(GREEN_POS);

				} else {

					setHoodPos(YELLOW_POS);

				}

				// If the shoot button is pressed, run the motors
				if (button("shoot")) {

					hatch.set(HATCH_CLOSE_POS);

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

					// If time is above threshold and lifter not too high
					if (timer.get() > SHOOT_DELAY && limit.get()) lifter.set(LIFTER_POW);
					else lifter.set(0);

				} else {

					hatch.set(HATCH_OPEN_POS);

					// Stop shooter
					leftShooter.set(0);
					rightShooter.set(0);

					// Also reset threshold time when not shooting
					belowThresholdTime = 0;
					timer.reset();

					// Detect if shoot button pressed once
					pressedOnce = false;

					// Stop overriding drive
					//drive.stopOverride();

					// Lower magazine when not shooting
				 	if (getLifterPos() < 0) lifter.set(-LIFTER_POW);
					else lifter.set(0);

				}
/*
				// Manually control lifter
				if (button("lift"))	lifter.set(LIFTER_POW);
				else if (button("lower")) lifter.set(-LIFTER_POW);
				else lifter.set(0);
*/
				
/*
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
				*/
				
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
		builder.addBooleanProperty("Limit", () -> limit.get(), null);

		if (hoodMode == MODES.def) builder.addBooleanProperty("DEF", () -> true, null);
		else builder.addBooleanProperty("DEF", () -> false, null);
		
	}

}
