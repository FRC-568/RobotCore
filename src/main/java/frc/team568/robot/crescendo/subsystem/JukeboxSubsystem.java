package frc.team568.robot.crescendo.subsystem;

import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kCANBusName;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kIntakePort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kIntakePower;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kLeftOuttakePort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kNoteDetectionRange;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kNoteDetectorPort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kOuttakeMaxRPS;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kOuttakeMinRPS;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kOuttakePID;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kRightOuttakePort;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.robot.crescendo.command.SpinUp;

public class JukeboxSubsystem extends SubsystemBase {
	// === motors ===
	private final TalonFX leftOuttakeMotor;
	private final TalonFX rightOuttakeMotor;
	private final PWMVictorSPX intakeMotor;

	private final ColorSensorV3 distanceSensor;

	private final VelocityVoltage outtakeVelocity;
	private final NeutralOut outtakeNeutral;

	private double outtakeAutoSpeed = kOuttakeMaxRPS;
	private double outtakeLeftBias = 0.0;
	private double lastOuttakeSpeedL, lastOuttakeSpeedR;

	public JukeboxSubsystem() {
		leftOuttakeMotor = new TalonFX(kLeftOuttakePort, kCANBusName);
		addChild("leftOuttakeMotor", leftOuttakeMotor);

		rightOuttakeMotor = new TalonFX(kRightOuttakePort, kCANBusName);
		addChild("rightOuttakeMotor", rightOuttakeMotor);

		TalonFXConfiguration outtakeMotorConfig = new TalonFXConfiguration()
				.withMotorOutput(
					new MotorOutputConfigs()
					.withNeutralMode(NeutralModeValue.Coast)
					.withInverted(InvertedValue.CounterClockwise_Positive)
				)
				.withSlot0(kOuttakePID);

		leftOuttakeMotor.getConfigurator().apply(outtakeMotorConfig);
		outtakeMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
		rightOuttakeMotor.getConfigurator().apply(outtakeMotorConfig);
		leftOuttakeMotor.optimizeBusUtilization();
		rightOuttakeMotor.optimizeBusUtilization();

		outtakeVelocity = new VelocityVoltage(0, 100, false, outtakeAutoSpeed, 0, false, false, false);
		outtakeNeutral = new NeutralOut();

		intakeMotor = new PWMVictorSPX(kIntakePort);
		intakeMotor.setInverted(true);
		addChild("intakeMotor", intakeMotor);

		distanceSensor = new ColorSensorV3(kNoteDetectorPort);
	}

	public void runOuttake() {
		runOuttake(outtakeAutoSpeed);
	}

	public void runOuttake(double speedRPS) {
		if (speedRPS < kOuttakeMinRPS) {
			stopOuttake();
			return;
		}

		speedRPS = MathUtil.clamp(speedRPS, kOuttakeMinRPS, kOuttakeMaxRPS);
		double lSpeed = speedRPS;
		double rSpeed = speedRPS;
		if (outtakeLeftBias != 0) {
			double biasedSpeed = MathUtil.clamp(speedRPS * (1.0 - Math.abs(outtakeLeftBias)), kOuttakeMinRPS,
					kOuttakeMaxRPS);
			if (outtakeLeftBias > 0)
				lSpeed = biasedSpeed;
			else if (outtakeLeftBias < 0)
				rSpeed = biasedSpeed;
		}
		runOuttakeManual(lSpeed, rSpeed);
	}

	public void runOuttakeManual(double lSpeedRPS, double rSpeedRPS) {
		lastOuttakeSpeedL = lSpeedRPS;
		lastOuttakeSpeedR = rSpeedRPS;
		leftOuttakeMotor.setControl(outtakeVelocity.withVelocity(lSpeedRPS));
		rightOuttakeMotor.setControl(outtakeVelocity.withVelocity(rSpeedRPS));
	}

	public void stopOuttake() {
		lastOuttakeSpeedL = 0;
		lastOuttakeSpeedR = 0;
		leftOuttakeMotor.setControl(outtakeNeutral);
		rightOuttakeMotor.setControl(outtakeNeutral);
	}

	public void setOuttakeAutoSpeed(double speedRPS) {
		outtakeAutoSpeed = speedRPS;
	}

	public void setOuttakeBias(double percentLeftSpin) {
		outtakeLeftBias = percentLeftSpin;
	}

	public boolean isOuttakeAtSpeed() {
		double error = leftOuttakeMotor.getClosedLoopError().getValueAsDouble();
		double target = leftOuttakeMotor.getClosedLoopReference().getValueAsDouble();
		return  error / target <= 0.05;
	}

	public void runIntake() {
		runIntake(kIntakePower);
	}

	public void runIntake(double percentOutput) {
		intakeMotor.set(percentOutput);
	}

	public void stopIntake() {
		intakeMotor.set(0);
	}

	public Command getBackoffIntakeCommand() {
		return Commands.run(() -> runIntake(-0.1), this).withTimeout(0.25);
	}

	public Command getLaunchNoteCommand() {
		return Commands.sequence(
				getBackoffIntakeCommand(),
				Commands.deadline(
                		Commands.waitUntil(this::isOuttakeAtSpeed).withTimeout(2)
								.andThen(Commands.run(this::runIntake).withTimeout(1)),
        				new SpinUp(this)));
	}

	public void initDefaultCommand(final DoubleSupplier intakeSpeed, final DoubleSupplier outtakeSpeed) {
		setDefaultCommand(new Command() {

			{
				addRequirements(JukeboxSubsystem.this);
			}

			@Override
			public void execute() {
				runIntake(intakeSpeed.getAsDouble());
				runOuttake(outtakeSpeed.getAsDouble() * kOuttakeMaxRPS);
			}

			@Override
			public void end(boolean interrupted) {
				// if (!interrupted) {
				// stopIntake();
				// stopOuttake();
				// }
			}

		});
	}

	public boolean hasNote() {
		return getDistance() > kNoteDetectionRange;
	}

	public double getLeftVelo() {
		return leftOuttakeMotor.getVelocity().getValueAsDouble();
	}

	public double getRightVelo() {
		return rightOuttakeMotor.getVelocity().getValueAsDouble();
	}

	public double getLeftDesiredVelo() {
		return lastOuttakeSpeedL;
	}

	public double getRightDesiredVelo() {
		return lastOuttakeSpeedR;
	}

	public double getLeftVoltage() {
		return leftOuttakeMotor.getMotorVoltage().getValueAsDouble();
	}

	public double getRightVoltage() {
		return rightOuttakeMotor.getMotorVoltage().getValueAsDouble();
	}

	public double getDistance() {
		return distanceSensor.getProximity();
	}

	@Override
	public void periodic() {

	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
	}
}
