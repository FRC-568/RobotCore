package frc.team568.robot.crescendo.subsystem;

<<<<<<< HEAD
=======
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
>>>>>>> 1c8a3f7 (Add open loop pivot control to gamepad)
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
<<<<<<< HEAD
=======
import edu.wpi.first.wpilibj.DutyCycle;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorTerm;

import edu.wpi.first.wpilibj2.command.Command;
>>>>>>> 1c8a3f7 (Add open loop pivot control to gamepad)
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.team568.robot.crescendo.Constants.PivotConstants.*;

public class PivotSubsystem extends SubsystemBase {
	// === motors ===
	private TalonFX leftMotor;
<<<<<<< HEAD
	private TalonFX rightMotor;

	private final double min = 0;
	private final double max = 0.5; // rotations

	DigitalInput limitSwitch = new DigitalInput(0);
=======
	private CANcoder leftMotorcCaNcoder;
    private TalonFX rightMotor;
	private DigitalInput limitSwitch = new DigitalInput(0);

	private final double min = 0;
	private final double max = 0.5*60*100; //rotations
>>>>>>> 1c8a3f7 (Add open loop pivot control to gamepad)

	boolean override = false;
	private DutyCycleOut openloopRequest = new DutyCycleOut(0.0);

<<<<<<< HEAD
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
=======
	public PivotSubsystem(int leftMotorPort, int rightMotorPort) {
		leftMotor = new TalonFX(leftMotorPort);

		leftMotor.setNeutralMode(NeutralModeValue.Coast); // Set neutral mode

		// HardwareLimitSwitchConfigs limitSwitchConfigs = new HardwareLimitSwitchConfigs();
		// limitSwitchConfigs.ReverseLimitAutosetPositionEnable = true;
		// limitSwitchConfigs.ReverseLimitAutosetPositionValue = 0;

		leftMotor.setControl(new DutyCycleOut(0)
		.withLimitForwardMotion(leftMotor.getPosition().getValueAsDouble() > max)
		.withLimitReverseMotion(limitSwitch.get()));
		
>>>>>>> 1c8a3f7 (Add open loop pivot control to gamepad)

		leftMotor = new TalonFX(kLeftMotorPort);
		addChild("leftMotor", leftMotor);
		leftMotor.getConfigurator().apply(motorConfig);
		leftMotor.setControl(new DutyCycleOut(0));

		rightMotor = new TalonFX(kRightMotorPort);
		addChild("rightMotor", rightMotor);
<<<<<<< HEAD
		rightMotor.getConfigurator().apply(motorConfig);
		rightMotor.setControl(new Follower(leftMotor.getDeviceID(), true));

		// manning
		// rightMotor.optimizeBusUtilization()
=======

		MotorOutputConfigs currentConfigs = new MotorOutputConfigs();
		currentConfigs.Inverted = InvertedValue.Clockwise_Positive; //TODO: reverse directions based on design
		
		leftMotor.getConfigurator().apply(currentConfigs);
		rightMotor.setControl(new Follower(leftMotor.getDeviceID(), true));

		// leftMotor.getConfigurator().apply(limitSwitchConfigs);
		// rightMotor.getConfigurator().apply(limitSwitchConfigs);

		//manning
		//rightMotor.optimizeBusUtilization()
		
		
		//=== pid configs ===
		//TODO: allow on the fly configuration
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = 0; //An error of 0.5 rotations and a value of 24 results in 12 V output
		slot0Configs.kI = 0; //no output for integrated error
		slot0Configs.kD = 0; //A velocity of 1 rps results in 0.1 V output at a setting of 0.1
		

		leftMotor.getConfigurator().apply(slot0Configs);
        
>>>>>>> 1c8a3f7 (Add open loop pivot control to gamepad)
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

<<<<<<< HEAD
	public void configurePID(double kP, double kI, double kD) {
=======
	public void setPower(double power) {
		leftMotor.setControl(openloopRequest.withOutput(power));
	}

	public void populate(double kP, double kI, double kD){
>>>>>>> 1c8a3f7 (Add open loop pivot control to gamepad)
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
