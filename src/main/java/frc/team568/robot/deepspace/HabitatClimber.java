package frc.team568.robot.deepspace;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class HabitatClimber extends SubsystemBase {

	final WPI_TalonSRX driveMotor;
	final WPI_TalonSRX liftMotorFront;
	final WPI_TalonSRX liftMotorBack;

	HabitatClimber(final int drivePort, final int frontPort, final int backPort) {
		driveMotor = new WPI_TalonSRX(drivePort);
		liftMotorFront = new WPI_TalonSRX(frontPort);
		liftMotorBack = new WPI_TalonSRX(backPort);
		
		liftMotorFront.setNeutralMode(NeutralMode.Brake);
		liftMotorBack.setNeutralMode(NeutralMode.Brake);

		addChild("Drive Motor", driveMotor);
		addChild("Front Lift Motor", liftMotorFront);
		addChild("Back Lift Motor", liftMotorBack);
	}

	ControlBuilder buildControlCommand() {
		return new ControlBuilder();
	}

	final class ControlBuilder {
		private DoubleSupplier frontControl = () -> 0;
		private DoubleSupplier backControl = () -> 0;
		private DoubleSupplier driveControl = () -> 0;
		
		private ControlBuilder() {}

		ControlBuilder withFront(DoubleSupplier control) {
			this.frontControl = control;
			return this;
		}

		ControlBuilder withBack(DoubleSupplier control) {
			this.backControl = control;
			return this;
		}

		ControlBuilder withDrive(DoubleSupplier control) {
			this.driveControl = control;
			return this;
		}

		Command makeCommand() {
			return new RunCommand(() -> {
				liftMotorFront.set(frontControl.getAsDouble());
				liftMotorBack.set(backControl.getAsDouble());
				driveMotor.set(driveControl.getAsDouble());
			}, HabitatClimber.this);
		}

		HabitatClimber makeDefault() {
			setDefaultCommand(makeCommand());
			return HabitatClimber.this;
		}
	}

}