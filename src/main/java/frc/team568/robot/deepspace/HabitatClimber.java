package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.RunCommand;
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
		
		liftMotorFront.setNeutralMode(NeutralMode.Brake);
		liftMotorBack.setNeutralMode(NeutralMode.Brake);

		addChild("Drive Motor", driveMotor);
		addChild("Front Lift Motor", liftMotorFront);
		addChild("Back Lift Motor", liftMotorBack);

		setDefaultCommand(new RunCommand(() -> {
			liftMotorFront.set(axis("climberFront"));
			liftMotorBack.set(axis("climberBack"));
			driveMotor.set(axis("climberDrive"));
		}, this));
	}

	@Override
	public String getConfigName() {
		return "climber";
	}
}