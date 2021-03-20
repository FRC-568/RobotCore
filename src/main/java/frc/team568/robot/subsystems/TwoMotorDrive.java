package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;

public abstract class TwoMotorDrive extends SubsystemBase {
	
	protected PowerDistributionPanel pdp;

	protected WPI_TalonSRX leftMotor;
	protected WPI_TalonSRX rightMotor;

	protected Gyro gyro = new ADXRS450_Gyro();

	protected double maxLeftCurrent = 0;
	protected double maxRightCurrent = 0;

	protected boolean safeMode = false;

	protected boolean overrrideMode = false;

	public TwoMotorDrive(RobotBase robot) {

		super(robot);

		// Initialize code
		initMotors();

		pdp = new PowerDistributionPanel();

		initDefaultCommand();
		
	}

	private void initMotors() {
		
		leftMotor = new WPI_TalonSRX(port("leftMotor"));
		rightMotor = new WPI_TalonSRX(port("rightMotor"));

		addChild("Left Motor", leftMotor);
		addChild("Right Motor", rightMotor);

		leftMotor.setInverted(true);
		rightMotor.setInverted(false);
		
	}

	@Override
	public void periodic() {
		
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

		leftMotor.set(0);
		rightMotor.set(0);

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

	public int getLeftPos() {

		return leftMotor.getSelectedSensorPosition();

	}
	
	public int getRightPos() {

		return rightMotor.getSelectedSensorPosition();

	}

	public double getLeftCurrent() {

		return pdp.getCurrent(port("leftMotor"));

	}

	public double getRightCurrent() {

		return pdp.getCurrent(port("rightMotor"));

	}

	protected void initDefaultCommand() {
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
				double leftPower = axis("forward") + axis("turn");
				double rightPower = axis("forward") - axis("turn");
				double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
				if (max > 1.0) {

					leftPower /= max;
					rightPower /= max;

				}

				// Set motor powers
				if (!overrrideMode) {

					leftMotor.set(leftPower * driveDamp);
					rightMotor.set(rightPower * driveDamp);

				}

				// Set the maximum current
				if (maxLeftCurrent < getLeftCurrent()) maxLeftCurrent = getLeftCurrent();
				if (maxRightCurrent < getRightCurrent()) maxRightCurrent = getRightCurrent();
				
				// Custom updates
				update();

			}

		}); // End set default command

	} // End init default command

	protected abstract void update();

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);

		builder.addDoubleProperty("Forward", () -> axis("forward"), null);
		builder.addDoubleProperty("Side", () -> axis("side"), null);
		builder.addDoubleProperty("Turn", () -> axis("turn"), null);
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
