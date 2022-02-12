package frc.team568.robot.deepspace;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class Lift extends SubsystemBase {
	// Minimum and maximum height
	private static double minHeight = -999999999;
	private static double maxHeight = 999999999;

	WPI_TalonSRX motor;
	DigitalInput homeSwitch;

	Lift(final int motorPort, final int homeSwitchPort) {
		motor = new WPI_TalonSRX(motorPort);
		addChild("Motor", motor);

		motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		motor.setSensorPhase(true);
		motor.setSelectedSensorPosition(0);

		homeSwitch = new DigitalInput(homeSwitchPort);

		addChild("Reset Problem Encoder",
			new InstantCommand(() -> motor.setSelectedSensorPosition(0), this));
	}

	public double getPosition() {
		return motor.getSelectedSensorPosition();
	}

	public Lift withControl(final DoubleSupplier liftControl) {
		setDefaultCommand(new CommandBase() {
			
			{ addRequirements(Lift.this); }

			@Override
			public void execute() {
				double position = getPosition();
				double input = liftControl.getAsDouble();
				// Stops the motor when passes MIN_HEIGHT or MAX_HEIGHT
				if (position < minHeight && input < 0 || position > maxHeight && input > 0)
					motor.stopMotor();
				else 
					motor.set(input);

				// Calibration point
				if (homeSwitch.get());
					motor.setSelectedSensorPosition(0);
			}
			
		});
		return Lift.this;
	}

	public class MoveToCommand extends CommandBase {
		private final DoubleSupplier targetValueSupplier;
		private double targetPosition;

		MoveToCommand(final DoubleSupplier targetValueSupplier){
			this.targetValueSupplier = targetValueSupplier;
			addRequirements(Lift.this);
		}

		@Override
		public void initialize() {
			targetPosition = targetValueSupplier.getAsDouble();
		}

		@Override
		public void execute() {
			motor.set(ControlMode.Position, targetPosition);
		}

		@Override
		public boolean isFinished() {
			return Math.abs(targetPosition - getPosition()) < 50;
		}
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Min height", () -> minHeight, (value) -> minHeight = value);
		builder.addDoubleProperty("Max height", () -> maxHeight, (value) -> maxHeight = value);
		builder.addDoubleProperty("Current height", () -> getPosition(), null);
	}

}

