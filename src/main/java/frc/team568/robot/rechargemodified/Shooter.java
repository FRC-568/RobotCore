package frc.team568.robot.rechargemodified;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class Shooter extends SubsystemBase {

	private WPI_TalonSRX leftShooter;
	private WPI_TalonSRX rightShooter;

	public Shooter(RobotBase robot) {

		super(robot);

		// Initialize code
		initMotors();

		reset();
		initDefaultCommand();
		
	}

	private void initMotors() {
		
		leftShooter = new WPI_TalonSRX(port("leftShooter"));
		rightShooter = new WPI_TalonSRX(port("rightShooter"));

		addChild("Left Shooter", leftShooter);
		addChild("Right Shooter", rightShooter);

		leftShooter.setInverted(true);
		rightShooter.setInverted(false);
		
	}

	@Override
	public void periodic() {
		
	}

	public void stop() {

	}

	public void reset() {
		
	}

	private double getLeftVel() {

		return leftShooter.getSelectedSensorVelocity();

	}

	private double getRightVel() {

		return rightShooter.getSelectedSensorVelocity();

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

				} else {

					leftShooter.set(0);
					rightShooter.set(0);

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
		
	}

}
