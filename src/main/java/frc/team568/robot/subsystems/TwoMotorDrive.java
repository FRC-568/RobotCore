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

public class TwoMotorDrive extends SubsystemBase {
	
	protected PowerDistributionPanel pdp;

	protected WPI_TalonSRX leftMotor;
	protected WPI_TalonSRX rightMotor;

	protected Gyro gyro;

	protected double maxLeftCurrent = 0;
	protected double maxRightCurrent = 0;

	protected boolean safeMode = false;

	protected boolean overrrideMode = false;

	public TwoMotorDrive(RobotBase robot) {

		super(robot);

		gyro = new ADXRS450_Gyro();

		// Initialize code
		initMotors();

		pdp = new PowerDistributionPanel();
		
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
		
	}

	public void resetGyro() {

		gyro.reset();

	}

	public double getAngle() {

		return gyro != null ? gyro.getAngle() : 579475;

	}

	public void resetMotors() {

		leftMotor.setSelectedSensorPosition(0);
		rightMotor.setSelectedSensorPosition(0);

	}

	public void stop() {

		setLeft(0);
		setRight(0);

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
				double leftPower = axis("forward") - axis("turn") * 0.5;
				double rightPower = axis("forward") + axis("turn") * 0.5;
				double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
				if (max > 1.0) {

					leftPower /= max;
					rightPower /= max;

				}

				// Set motor powers
				if (!overrrideMode) {

					int opR;
					int opL;

					if (rightPower > 0) opR = 1;
					else opR = -1;

					if (leftPower > 0) opL = 1;
					else opL = -1;

					leftMotor.set(opL * Math.pow(leftPower * driveDamp, 2));
					rightMotor.set(opR * Math.pow(rightPower * driveDamp, 2));

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
