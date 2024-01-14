package frc.team568.robot.drive;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public final class Mk4iFactory {
	private static final double WHEEL_RADIUS = 0.047625; //3.75 inch wheels on mk4i
	private static final double NEO_ENCODER_UNITS = 42;
	//private static final double CANCODER_UNITS = 4096;

	private MotorController driveMotor;
	private MotorController turningMotor;
	private CANcoder turningEncoder;
	private DoubleSupplier drivePosition;
	private DoubleSupplier driveVelocity;
	private DoubleSupplier turningAngle;
	private DoubleSupplier turningRate;

	public Mk4iFactory withNeoDrive(int port) {
		var motor = new CANSparkMax(port, MotorType.kBrushless);
		driveMotor = motor;

		var encoder = motor.getEncoder();
		encoder.setVelocityConversionFactor(2 * Math.PI * WHEEL_RADIUS / NEO_ENCODER_UNITS / 60);
		encoder.setPositionConversionFactor(2 * Math.PI * WHEEL_RADIUS / NEO_ENCODER_UNITS);
		drivePosition = encoder::getPosition;
		driveVelocity = encoder::getVelocity;

		return this;
	}
	
	public Mk4iFactory withDriveMotor(MotorController driveMotor) {
		this.driveMotor = driveMotor;

		return this;
	}

	public Mk4iFactory withNeoTurning(int port) {
		var motor = new CANSparkMax(port, MotorType.kBrushless);
		turningMotor = motor;

		var encoder = motor.getEncoder();
		encoder.setVelocityConversionFactor(2 * Math.PI / NEO_ENCODER_UNITS / 60);
		encoder.setPositionConversionFactor(2 * Math.PI / NEO_ENCODER_UNITS);
		drivePosition = encoder::getPosition;
		driveVelocity = encoder::getVelocity;

		return this;
	}
	
	public Mk4iFactory withTurningMotor(MotorController turningMotor) {
		this.turningMotor = turningMotor;

		return this;
	}

	public Mk4iFactory withTurningCANCoder(int port) {
		turningEncoder = new CANcoder(port);

		// 2024 Migration - add conversion rate here is needed.

		turningAngle = () -> turningEncoder.getAbsolutePosition().getValueAsDouble() /* 2 * Math.PI / CANCODER_UNITS */;
		turningRate = () -> turningEncoder.getVelocity().getValueAsDouble() /* 2 * Math.PI / CANCODER_UNITS */;

		return this;
	}

	public SwerveModule build() {
		return new SwerveModule() {

			@Override
			public MotorController getDriveMotor() {
				return driveMotor;
			}

			@Override
			public MotorController getTurningMotor() {
				return turningMotor;
			}

			@Override
			public double getDistanceMeters() {
				return drivePosition.getAsDouble();
			}

			@Override
			public Rotation2d getRotation() {
				return Rotation2d.fromRadians(turningAngle.getAsDouble());
			}

			@Override
			public double getVelocityMetersPerSecond() {
				return driveVelocity.getAsDouble();
			}

			@Override
			public double getRotationRateRadians() {
				return turningRate.getAsDouble();
			}

			public void setDesiredState(SwerveModuleState desiredState) {
				//TO-DO: use PID and Feedforward to apply values here
			}
		};
	}
}
