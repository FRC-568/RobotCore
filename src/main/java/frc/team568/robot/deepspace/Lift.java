package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX.M

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Lift extends SubsystemBase {
	WPI_TalonSRX liftMotor;

	DigitalInput homeSwitch;

	// Minimum and maximum height
	private static double minHeight = 0;
	private static double maxHeight = 400000;

	private NetworkTableEntry hatch1, hatch2, hatch3, cargo1, cargo2, cargo3;

	Lift(RobotBase robot) {
		super(robot);
		liftMotor = new WPI_TalonSRX(configInt("motorLift"));
		addChild(liftMotor);

		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		liftMotor.setSensorPhase(true);

		homeSwitch = new DigitalInput(configInt("homeSwitch"));
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

				// Calibration point
				//if (homeSwitch.get());
				//	liftMotor.setSelectedSensorPosition(0);
			
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});

	}

	public Command getCommandLiftTo1() {
		return new MoveToCommand(hatch1, cargo1);
	}

	private class MoveToCommand extends Command {
		private final NetworkTableEntry hatchEntry;
		private final NetworkTableEntry cargoEntry;
		private boolean forCargo = false;
		private double targetPosition = 0;

		MoveToCommand(final NetworkTableEntry hatchEntry, final NetworkTableEntry cargoEntry){
			requires(Lift.this);
			this.hatchEntry = hatchEntry;
			this.cargoEntry = cargoEntry;
		}

		@Override
		protected void initialize() {
			forCargo = button("liftForCargo");
			double pos = liftMotor.getSelectedSensorPosition();
			targetPosition = forCargo ?  cargoEntry.getDouble(pos) : hatchEntry.getDouble(pos);
		}

		@Override
		protected void execute() {
			liftMotor.set(ControlMode.Position, targetPosition);
		}

		@Override
		protected boolean isFinished() {
			return false;
		}
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Min height", () -> minHeight, (value) -> minHeight = value);
		builder.addDoubleProperty("Max height", () -> maxHeight, (value) -> maxHeight = value);
		builder.addDoubleProperty("Current height", () -> getPosition(), null);

		hatch1 = builder.getEntry("Hatch 1");
		hatch2 = builder.getEntry("Hatch 2");
		hatch3 = builder.getEntry("Hatch 3");
		hatch1.setPersistent();
		hatch2.setPersistent();
		hatch3.setPersistent();

		cargo1 = builder.getEntry("Cargo 1");
		cargo2 = builder.getEntry("Cargo 2");
		cargo3 = builder.getEntry("Cargo 3");
		cargo1.setPersistent();
		cargo2.setPersistent();
		cargo3.setPersistent();
	}

}