package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Lift extends SubsystemBase {
	WPI_TalonSRX liftMotor;

	Lift(RobotBase robot) {
		super(robot);

		liftMotor = new WPI_TalonSRX(configInt("motorLift"));
		addChild(liftMotor);
	}

	@Override
	public String getConfigName() {
		return "lift";
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Command() {
			{ requires(Lift.this); }

			@Override
			protected void execute() {
				liftMotor.set(axis("lift"));
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});
	}
}