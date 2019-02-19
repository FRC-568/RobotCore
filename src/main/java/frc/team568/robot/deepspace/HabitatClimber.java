package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class HabitatClimber extends SubsystemBase {
	WPI_TalonSRX driveMotor;
	WPI_TalonSRX liftMotorFront;
	WPI_TalonSRX liftMotorBack;

	HabitatClimber(RobotBase robot) {
		super(robot);

		driveMotor = new WPI_TalonSRX(configInt("motorDrive"));
		liftMotorFront = new WPI_TalonSRX(configInt("motorClimbFront"));
		liftMotorBack = new WPI_TalonSRX(configInt("motorClimbBack"));

		addChild(driveMotor);
		addChild(liftMotorFront);
		addChild(liftMotorBack);
	}

	@Override
	public String getConfigName() {
		return "climber";
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Command() {
			{ requires(HabitatClimber.this); }

			@Override
			protected void execute() {
				liftMotorFront.set(axis("climberFront"));
				liftMotorBack.set(axis("climberBack"));
				driveMotor.set(axis("climberDrive"));
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}
}