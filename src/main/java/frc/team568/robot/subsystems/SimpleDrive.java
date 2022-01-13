package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;

public class SimpleDrive extends SubsystemBase {

	protected WPI_TalonSRX leftMotor;
	protected WPI_TalonSRX rightMotor;

	public SimpleDrive(RobotBase robot) {

		super(robot);

		leftMotor = new WPI_TalonSRX(port("leftMotor"));
		rightMotor = new WPI_TalonSRX(port("rightMotor"));

		addChild("Left Motor", leftMotor);
		addChild("Right Motor", rightMotor);

		leftMotor.setInverted(false);
		rightMotor.setInverted(true);
		
	}
	
	public void initDefaultCommand() {

		setDefaultCommand(new CommandBase() {

			{
				addRequirements(SimpleDrive.this);
				SendableRegistry.addChild(SimpleDrive.this, this);
			}

			@Override
			public void execute() {

				double leftPower = axis("forward") * 0.5 - axis("turn") * 0.3;
				double rightPower = axis("forward") * 0.5 + axis("turn") * 0.3;
				double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
				if (max > 1.0) {

					leftPower /= max;
					rightPower /= max;

				}

				leftMotor.set(leftPower);
				rightMotor.set(rightPower);

			}

		});

	}

}