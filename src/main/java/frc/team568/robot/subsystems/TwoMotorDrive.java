package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;

public class TwoMotorDrive extends SubsystemBase {
	
	private PowerDistributionPanel pdp;

	private WPI_TalonSRX leftMotor;
	private WPI_TalonSRX rightMotor;

	private double maxLeftCurrent = 0;
	private double maxRightCurrent = 0;

	private boolean safeMode = false;

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

	private double getLeftVel() {

		return leftMotor.getSelectedSensorVelocity();

	}

	private double getRightVel() {

		return rightMotor.getSelectedSensorVelocity();

	}

	private int getLeftPos() {

		return leftMotor.getSelectedSensorPosition();

	}
	
	private int getRightPos() {

		return rightMotor.getSelectedSensorPosition();

	}

	private double getLeftCurrent() {

		return pdp.getCurrent(port("leftMotor"));

	}

	private double getRightCurrent() {

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
				leftMotor.set(leftPower * driveDamp);
				rightMotor.set(rightPower * driveDamp);

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
