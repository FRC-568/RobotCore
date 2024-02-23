package frc.team568.robot.crescendo.subsystem;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class JukeboxSubsystem extends SubsystemBase {
    //=== motors ===
	private TalonFX leftOuttakeMotor;
    private TalonFX rightOuttakeMotor;
	private CANSparkMax intakeMotor;

	final private VelocityVoltage velocity = new VelocityVoltage(0);


	boolean override = false;

	public JukeboxSubsystem(int leftOuttakeMotorPort, int rightOuttakeMotorPort, int intakeMotorPort) {
		leftOuttakeMotor = new TalonFX(leftOuttakeMotorPort);
		addChild("leftOuttakeMotor", leftOuttakeMotor);

		rightOuttakeMotor = new TalonFX(rightOuttakeMotorPort);
		addChild("rightOuttakeMotor", rightOuttakeMotor);

		intakeMotor = new CANSparkMax(intakeMotorPort, MotorType.kBrushless);
		addChild("intakeMotor", builder -> {
			builder.addDoubleProperty("output", intakeMotor::get, null);
		});
		
		MotorOutputConfigs lConfigs = new MotorOutputConfigs();
		lConfigs.Inverted = InvertedValue.CounterClockwise_Positive ; //TODO: reverse directions based on design

		MotorOutputConfigs rConfigs = new MotorOutputConfigs();
		rConfigs.Inverted = InvertedValue.Clockwise_Positive;
        
        //leftOuttakeMotor.selectProfileSlot(0, 0);
        //leftOuttakeMotor.configClosedloopRamp(0.1);

		leftOuttakeMotor.getConfigurator().apply(lConfigs);
		rightOuttakeMotor.getConfigurator().apply(rConfigs);

		intakeMotor.setInverted(true);

		//TODO: Make invert intake motor based on design

		velocity.Slot = 0;
		
		//=== pid configs ===
		//TODO: allow on the fly configuration
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = 0; //An error of 0.5 rotations and a value of 24 results in 12 V output
		slot0Configs.kI = 0; //no output for integrated error
		slot0Configs.kD = 0; //A velocity of 1 rps results in 0.1 V output at a setting of 0.1

		leftOuttakeMotor.getConfigurator().apply(slot0Configs);
		rightOuttakeMotor.getConfigurator().apply(slot0Configs);
	}
	
	/**
	 * 
	 * @param speed rps
	 */
	public void setOuttakeSpeed(double speed) {
		setOuttakeSpeed(speed, speed);
	}

	/**
	 * 
	 * @param lSpeed rps
	 * @param rSpeed rps
	 */
	public void setOuttakeSpeed(double lSpeed, double rSpeed) {
		leftOuttakeMotor.setControl(velocity.withVelocity(lSpeed));
		rightOuttakeMotor.setControl(velocity.withVelocity(rSpeed));
	}

	public void setIntakeSpeed(double speed){
		intakeMotor.set(speed);
	}

	public void initDefaultCommand(final DoubleSupplier intakeSpeed, final DoubleSupplier outtakeSpeedL, final DoubleSupplier outtakeSpeedR) {
		setDefaultCommand(new Command() {

			{ addRequirements(JukeboxSubsystem.this); }

			@Override
			public void execute() {
				setIntakeSpeed(intakeSpeed.getAsDouble());
				setOuttakeSpeed(outtakeSpeedL.getAsDouble(), outtakeSpeedR.getAsDouble());
			}

			@Override
			public void end(boolean interrupted) {
				setIntakeSpeed(0);
				setOuttakeSpeed(0);
			}

		});
	}

	@Override
	public void periodic() {
		
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		//builder.addDoubleProperty("Stage position", () -> getStagePos(), null);
	}
}
