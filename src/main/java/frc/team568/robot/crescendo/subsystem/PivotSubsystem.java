package frc.team568.robot.crescendo.subsystem;


import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycle;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorTerm;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.team568.robot.crescendo.Constants.PivotConstants.*;

public class PivotSubsystem extends SubsystemBase {
	// === motors ===
	private TalonFX leftMotor;
	private TalonFX rightMotor;

	private final double min = 0;
	private final double max = 0.5; // rotations

	DigitalInput limitSwitch = new DigitalInput(0);

	boolean override = false;
	private DutyCycleOut openloopRequest = new DutyCycleOut(0.0);
	public PivotSubsystem() {
		// TODO: Confirm if we are really using Falcon 500s for the pivot and if a physical limit switch will be installed.
		TalonFXConfiguration motorConfig = new TalonFXConfiguration()
				.withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast)
						.withInverted(InvertedValue.Clockwise_Positive))
				.withHardwareLimitSwitch(
						new HardwareLimitSwitchConfigs().withForwardLimitEnable(true).withReverseLimitEnable(true)
								.withReverseLimitAutosetPositionValue(0).withReverseLimitAutosetPositionEnable(true))
				.withSoftwareLimitSwitch(new SoftwareLimitSwitchConfigs().withForwardSoftLimitThreshold(max)
						.withForwardSoftLimitEnable(true).withReverseSoftLimitThreshold(min)
						.withReverseSoftLimitEnable(true))
				.withSlot0(new Slot0Configs().withKP(0.5).withKI(0).withKD(0.005));
//TODO: make this readable
		leftMotor = new TalonFX(kLeftMotorPort);
		addChild("leftMotor", leftMotor);
		leftMotor.getConfigurator().apply(motorConfig);
		leftMotor.setControl(new DutyCycleOut(0));

		rightMotor = new TalonFX(kRightMotorPort);
		addChild("rightMotor", rightMotor);
		rightMotor.getConfigurator().apply(motorConfig);
		rightMotor.setControl(new Follower(leftMotor.getDeviceID(), true));

		// manning
		// rightMotor.optimizeBusUtilization()
	}

	/**
	 * 
	 * @param angle degrees from ... somewhere
	 */
	// TODO: figure out where 0 is
	public void setAngle(double angle) {
		angle /= 360.0; // degrees to rotations
		final PositionVoltage request = new PositionVoltage(0).withSlot(0);

		// set position to 10 rotations
		leftMotor.setControl(request.withPosition(angle)
				.withLimitForwardMotion(leftMotor.getPosition().getValueAsDouble() > max)
				.withLimitReverseMotion(leftMotor.getPosition().getValueAsDouble() < min));
	}

	public double getAngle() {
		return 0;
	}

	public void setPower(double power) {
		leftMotor.setControl(openloopRequest.withOutput(power));
	}

	public void populate(double kP, double kI, double kD){
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = kP;
		slot0Configs.kI = kI;
		slot0Configs.kD = kD;

		leftMotor.getConfigurator().apply(slot0Configs);
	}

	public void aim(double distance) {
		double h = 12.34; // speaker height
		double d = distance;
		double a = Math.sqrt(d * d + h * h);
		double c = 5.67; // arm length
		double alpha = Math.toRadians(60.0); // angle between arm and jukebox

		double theta = Math.atan(h / d) + Math.PI - alpha - Math.asin((c / a) * Math.sin(alpha));

		setAngle(Math.toDegrees(theta));
	}

	public void setVoltage(double voltage) {
		leftMotor.setVoltage(voltage);
	}

	public boolean getSwitch() {
		return limitSwitch.get();
	}

	public void initDefaultCommand(final DoubleSupplier pivotPower) {
		setDefaultCommand(new Command() {

			{ addRequirements(PivotSubsystem.this); }

			@Override
			public void execute() {
				setPower(pivotPower.getAsDouble());
			}

			@Override
			public void end(boolean interrupted) {
				setPower(0);
			}

		});
	}

	@Override
	public void periodic() {

	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		// builder.addDoubleProperty("Stage position", () -> getStagePos(), null);

	}
}
