package frc.team568.robot.rechargemodified;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class Shooter extends SubsystemBase {

	private WPI_TalonFX leftShooter;
	private WPI_TalonFX rightShooter;

	private Timer timer = new Timer();
	private boolean pressedOnce = false;

	private static final double CPR = 2048;
	private static final double METERS_PER_INCHES = 1 / 39.37;
	private static final double WHEEL_DIAMETER = 6 * METERS_PER_INCHES;
	private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
	private static final double GEAR_RATIO = 24.0 / 32.0;

	private double belowThresholdTime = 0;
	private static final double THRESHOLD_SPEED = 9; // In m/s

	public Shooter(RobotBase robot) {

		super(robot);

		// Initialize code
		initMotors();

		initDefaultCommand();
		
	}

	private void initMotors() {
		
		leftShooter = new WPI_TalonFX(port("leftShooter"));
		rightShooter = new WPI_TalonFX(port("rightShooter"));

		addChild("Left Shooter", leftShooter);
		addChild("Right Shooter", rightShooter);

		leftShooter.setInverted(true);
		rightShooter.setInverted(false);
		
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

					leftShooter.set(1);
					rightShooter.set(1);
					
					if (!pressedOnce) {

						pressedOnce = true;

						// Start timer the moment driver shoots
						timer.start();

					}

					// If the average MPS lower than threshold speed, continue counting
					if ((getLeftVelMPS() + getRightVelMPS()) / 2 < THRESHOLD_SPEED) {

						belowThresholdTime = timer.get();

					}

				} else {

					leftShooter.set(0);
					rightShooter.set(0);

					// Also reset threshold time when not shooting
					belowThresholdTime = 0;

					timer.reset();

					pressedOnce = false;

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
		
	}

}
