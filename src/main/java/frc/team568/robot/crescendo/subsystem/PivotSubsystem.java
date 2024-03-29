package frc.team568.robot.crescendo.subsystem;

import static frc.team568.robot.crescendo.Constants.PivotConstants.kCANBusName;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kG;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kGearRatio;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kLeftMotorPort;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kMaxAngle;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kMinAngle;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kPidConstants;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kRightMotorPort;
import static frc.team568.robot.crescendo.Constants.PivotConstants.kStartingAngle;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.team568.robot.crescendo.Constants;
import frc.team568.robot.crescendo.RotationCalc;

public class PivotSubsystem extends SubsystemBase {
	// === motors ===
	private TalonFX leftMotor;
	private TalonFX rightMotor;

	RotationCalc rotCalc;

	private DutyCycleOut openloopRequest = new DutyCycleOut(0.0);
	private MotionMagicVoltage closedLoopRequest = new MotionMagicVoltage(0);
	//private PositionVoltage normalPid = new PositionVoltage(0);

	private MotorOutputConfigs outputConfig = new MotorOutputConfigs()
		.withNeutralMode(NeutralModeValue.Brake)
		.withInverted(InvertedValue.Clockwise_Positive)
		.withPeakForwardDutyCycle(0.4)
		.withPeakReverseDutyCycle(-0.4);

	
	private boolean brakeOnNeutral = true;
	private double targetAngle;
	private boolean holdTargetAngle = false;
	private boolean manualControlIsActive = false;

	public PivotSubsystem() {
		TalonFXConfiguration motorConfig = new TalonFXConfiguration()
				.withMotorOutput(
					outputConfig
				)
				.withHardwareLimitSwitch(
					new HardwareLimitSwitchConfigs()
					.withReverseLimitEnable(true)
					.withReverseLimitAutosetPositionEnable(true)
					.withReverseLimitAutosetPositionValue(
						degToRot(kMinAngle)
					)
				)
				.withSoftwareLimitSwitch(
					new SoftwareLimitSwitchConfigs()
					.withForwardSoftLimitEnable(true)
					.withForwardSoftLimitThreshold(
						degToRot(kMaxAngle)
					)
				)
				.withMotionMagic(
					Constants.PivotConstants.kMotionMagicConfigs
				)
				.withSlot0(
					kPidConstants
				);


		leftMotor = new TalonFX(kLeftMotorPort, kCANBusName);
		addChild("leftMotor", leftMotor);
		leftMotor.getConfigurator().apply(motorConfig);
		leftMotor.setControl(new DutyCycleOut(0));
		// leftMotor.optimizeBusUtilization();

		rightMotor = new TalonFX(kRightMotorPort, kCANBusName);
		addChild("rightMotor", rightMotor);
		rightMotor.getConfigurator().apply(motorConfig);
		rightMotor.setControl(new Follower(leftMotor.getDeviceID(), true));
		// rightMotor.optimizeBusUtilization();

		leftMotor.setPosition(degToRot(kStartingAngle)); // Assume the pivot is vertical at power on
	}

	public void toggleBrakes() {
		brakeOnNeutral = !brakeOnNeutral;
		outputConfig.NeutralMode = brakeOnNeutral ? NeutralModeValue.Brake : NeutralModeValue.Coast;
	
		leftMotor.getConfigurator().apply(outputConfig);
		rightMotor.getConfigurator().apply(outputConfig);
	}

	public void moveToward(double angle) {
		angle = MathUtil.clamp(angle, kMinAngle, kMaxAngle);
		targetAngle = angle;
		leftMotor.setControl(
			closedLoopRequest.withPosition(degToRot(angle))
			.withFeedForward(kG * Math.cos(Math.toRadians(getAngle())))
		);
	}

	public void setTargetAngle(double angle) {
		angle = MathUtil.clamp(angle, kMinAngle, kMaxAngle);
		targetAngle = angle;
		holdTargetAngle = true;
	}

	public void clearTargetAngle() {
		holdTargetAngle = false;
	}

	public double getAngle() {
		return rotToDeg(leftMotor.getPosition().getValueAsDouble());
	}

	public Optional<Double> getTargetAngle() {
		return Optional.ofNullable(holdTargetAngle ? targetAngle : null);
	}

	public double getVelocity() {
		return rotToDeg(leftMotor.getVelocity().getValueAsDouble());
	}

	public double getVoltage() {
		return leftMotor.getMotorVoltage().getValueAsDouble();
	}

	public double getClosedLoopReference() {
		return rotToDeg(leftMotor.getClosedLoopReference().getValueAsDouble());
	}

	public double getClosedLoopReferenceSlope() {
		return rotToDeg(leftMotor.getClosedLoopReferenceSlope().getValueAsDouble());
	}

	public void setPower(double power) {
		leftMotor.setControl(openloopRequest.withOutput(power));
		// leftMotor.clearStickyFault_ForwardSoftLimit
	}

	public void populate(double kP, double kI, double kD) {
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = kP;
		slot0Configs.kI = kI;
		slot0Configs.kD = kD;

		leftMotor.getConfigurator().apply(slot0Configs);
	}

	public void aim(boolean isSpeaker) {
		aim(rotCalc.checkTotDistance(isSpeaker));
	}

	public void aim(double distance) {
		double h = 12.34; // speaker height
		double d = distance;
		double a = Math.sqrt(d * d + h * h);
		double c = 5.67; // arm length
		double alpha = Math.toRadians(60.0); // angle between arm and jukebox

		double theta = Math.atan(h / d) + Math.PI - alpha - Math.asin((c / a) * Math.sin(alpha));

		moveToward(Math.toDegrees(theta));
	}

	public void setVoltage(double voltage) {
		// leftMotor.setVoltage(voltage);
	}

	public boolean getSwitch() {
		return leftMotor.getReverseLimit().getValue() == ReverseLimitValue.ClosedToGround;
	}

	public static final double degToRot(double degrees) {
		return (degrees / 360.0) * kGearRatio;
	}

	public static final double rotToDeg(double rotations) {
		return (rotations / kGearRatio) * 360.0;
	}

	public void initDefaultCommand(final DoubleSupplier pivotPower) {
		setDefaultCommand(new Command() {

			{
				addRequirements(PivotSubsystem.this);
			}

			@Override
			public void execute() {
				double power = pivotPower.getAsDouble();

				if (power == 0) {
					if (manualControlIsActive) {
						manualControlIsActive = false;
						setTargetAngle(getAngle());
					}

					if (holdTargetAngle)
						moveToward(targetAngle);
					else
						setPower(0);
				} else {
					manualControlIsActive = true;
					setPower(power);
				}
			}

			@Override
			public void end(boolean interrupted) {
				// setPower(0);
			}

		});
	}

	@Override
	public void periodic() {

	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
	}
}
