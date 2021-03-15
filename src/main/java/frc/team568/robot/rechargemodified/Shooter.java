package frc.team568.robot.rechargemodified;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class Shooter extends SubsystemBase {

	// Motors
	private WPI_TalonFX leftShooter;
	private WPI_TalonFX rightShooter;
	private WPI_TalonFX lifter;

	// Timer
	private Timer timer = new Timer();
	private boolean pressedOnce = false;
	private double belowThresholdTime = 0;
	private double thresholdSpeed = 9; // In m/s

	// Calculation variables
	private static final double CPR = 2048;
	private static final double METERS_PER_INCHES = 1 / 39.37;
	private static final double WHEEL_DIAMETER = 6 * METERS_PER_INCHES;
	private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
	private static final double GEAR_RATIO = 24.0 / 32.0;

	// Final variables
	private static final double SHOOT_POW = 1;
	private static final double LIFTER_POW = 0.2;
	private static final double LIFTER_HIGH = 5000;

	public Shooter(RobotBase robot) {

		super(robot);

		// Initialize code
		initMotors();

		initDefaultCommand();
		
	}

	private void initMotors() {
		
		leftShooter = new WPI_TalonFX(port("leftShooter"));
		rightShooter = new WPI_TalonFX(port("rightShooter"));
		lifter = new WPI_TalonFX(port("lifter"));

		addChild("Left Shooter", leftShooter);
		addChild("Right Shooter", rightShooter);
		addChild("Lifter", lifter);

		leftShooter.setInverted(true);
		rightShooter.setInverted(false);
		lifter.setInverted(false);
		
	}

	@Override
	public void periodic() {
		
	}

	private double getLeftVel() {

		return leftShooter.getSelectedSensorVelocity();

	}

	private double getRightVel() {

		return rightShooter.getSelectedSensorVelocity();

	}

	private double getLeftVelMPS() {

		return getLeftVel() * (10.0 / CPR) * WHEEL_CIRCUMFERENCE * (1 / GEAR_RATIO);

	}

	private double getRightVelMPS() {

		return getRightVel() * (10.0 / CPR) * WHEEL_CIRCUMFERENCE * (1 / GEAR_RATIO);

	}

	private int getLeftPos() {

		return leftShooter.getSelectedSensorPosition();

	}
	
	private int getRightPos() {

		return rightShooter.getSelectedSensorPosition();

	}

	private int getLifterPos() {

		return lifter.getSelectedSensorPosition();

	}

	protected void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {

			{
				addRequirements(Shooter.this);
				SendableRegistry.addChild(Shooter.this, this);
			}

			@Override
			public void execute() {
				
				// If the shoot button is pressed, run the motors
				if (button("shoot")) {

					// Set motor power
					double voltage = RobotController.getBatteryVoltage();
					final double POW_PER_VOLT = 0.1;
					double correction = (13 - voltage) * POW_PER_VOLT;
					leftShooter.set(SHOOT_POW + correction);
					rightShooter.set(SHOOT_POW + correction);
					
					if (!pressedOnce) {

						pressedOnce = true;

						// Start timer the moment driver shoots
						timer.start();

					}

					if ((getLeftVelMPS() + getRightVelMPS()) / 2 < thresholdSpeed) {

						// If the average MPS lower than threshold speed, continue counting
						belowThresholdTime = timer.get();

					} else {

						// If above threshold speed and below max height, lift magazine
						if (getLifterPos() < LIFTER_HIGH) lifter.set(LIFTER_POW);
						else lifter.set(0);

					}

				} else {

					// Stop shooter
					leftShooter.set(0);
					rightShooter.set(0);

					// Also reset threshold time when not shooting
					belowThresholdTime = 0;
					timer.reset();

					// Detect if shoot button pressed once
					pressedOnce = false;

					// Lower magazine when not shooting
					if (getLifterPos() > 0) lifter.set(-LIFTER_POW);
					else lifter.set(0);

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
		builder.addDoubleProperty("Left Shooter Velocity (m/s)", () -> getLeftVelMPS(), null);
		builder.addDoubleProperty("Right Shooter Velocity (m/s)", () -> getRightVelMPS(), null);
		builder.addDoubleProperty("Time Below Threshold (m/s)", () -> belowThresholdTime, null);
		builder.addDoubleProperty("Shoot Threshold Speed (m/s)", () -> thresholdSpeed, (value) -> thresholdSpeed = value);
		
	}

}
