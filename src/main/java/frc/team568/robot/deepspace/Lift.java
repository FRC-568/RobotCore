package frc.team568.robot.deepspace;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Lift extends SubsystemBase {
	WPI_TalonSRX liftMotor;
	DigitalInput homeSwitch;

	// Minimum and maximum height
	private static double minHeight = -999999999;
	private static double maxHeight = 999999999;

	private NetworkTableEntry hatch1, hatch2, hatch3, cargo1, cargo2, cargo3;

	Lift(RobotBase robot) {
		super(robot);
		liftMotor = new WPI_TalonSRX(configInt("motorLift"));
		addChild(liftMotor);

		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		liftMotor.setSensorPhase(true);
		liftMotor.setSelectedSensorPosition(0);

		homeSwitch = new DigitalInput(configInt("homeSwitch"));

		SmartDashboard.putData("Reset Problem Encoder",
			new InstantCommand("Reset Problem Encoder", this, () -> liftMotor.setSelectedSensorPosition(0)));
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
				NetworkTableEntry target;
				
				if(button("liftToPosition1")) {
					target = button("liftForCargo") ? cargo1 : hatch1;
					if(button("bookmarkButton"))
						target.setDouble(liftMotor.getSelectedSensorPosition());
					else
						new MoveToCommand(target, () -> !button("liftToPosition1"));
				} else if(button("liftToPosition2")) {
					target = button("liftForCargo") ? cargo2 : hatch2;
					if(button("bookmarkButton"))
						target.setDouble(liftMotor.getSelectedSensorPosition());
					else
						new MoveToCommand(target, () -> !button("liftToPosition2"));
				} else if(button("liftToPosition3")) {
					target = button("liftForCargo") ? cargo3 : hatch3;
					if(button("bookmarkButton"))
						target.setDouble(liftMotor.getSelectedSensorPosition());
					else
						new MoveToCommand(target, () -> !button("liftToPosition3"));
				} else if (getPosition() < minHeight && axis("lift") < 0) { // Stops the motor when passes MIN_HEIGHT or MAX_HEIGHT
					liftMotor.stopMotor();
				} else if (getPosition() > maxHeight && axis("lift") > 0)
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

	private class MoveToCommand extends Command {
		private final NetworkTableEntry targetEntry;
		private double targetPosition = 0;
		private BooleanSupplier condition;

		MoveToCommand(final NetworkTableEntry targetEntry, BooleanSupplier finishedCondition){
			requires(Lift.this);
			this.targetEntry = targetEntry;
			condition = finishedCondition;
		}

		@Override
		protected void initialize() {
			double pos = liftMotor.getSelectedSensorPosition();
			targetPosition = targetEntry.getDouble(pos);
		}

		@Override
		protected void execute() {
			liftMotor.set(ControlMode.Position, targetPosition);
		}

		@Override
		protected boolean isFinished() {
			return condition.getAsBoolean();
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

