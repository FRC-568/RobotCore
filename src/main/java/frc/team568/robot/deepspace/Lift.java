package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Lift extends SubsystemBase {
	WPI_TalonSRX liftMotor;

	// Minimum and maximum height
	private static double minHeight = 0;
	private static double maxHeight = 4000;

	Lift(RobotBase robot) {
		super(robot);
		liftMotor = new WPI_TalonSRX(4);
		//liftMotor = new WPI_TalonSRX(configInt("motorLift"));
		addChild(liftMotor);

		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		liftMotor.setSensorPhase(true);

	}

	@Override
	public String getConfigName() {
		return "lift";
	}

	public double getPosition() {
		return liftMotor.getSelectedSensorPosition();
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Command() {
			
			{ requires(Lift.this); }

			@Override
			protected void execute() {

				// Stops the motor when passes MIN_HEIGHT or MAX_HEIGHT
				if (getPosition() < minHeight && axis("lift") < 0)
					liftMotor.stopMotor();
				else if (getPosition() > maxHeight && axis("lift") > 0)
					liftMotor.stopMotor();
				else 
					liftMotor.set(axis("lift"));

				// if (switchIsPressed);
				// 	liftMotor.setSelectedSensorPosition(0);
			
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});

	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Min height", () -> minHeight, (value) -> minHeight = value);
		builder.addDoubleProperty("Max height", () -> maxHeight, (value) -> maxHeight = value);
		builder.addDoubleProperty("Current height", this::getPosition, null);
	}

}