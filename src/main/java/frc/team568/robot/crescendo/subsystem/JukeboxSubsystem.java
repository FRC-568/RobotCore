package frc.team568.robot.crescendo.subsystem;

import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kIntakePort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kLeftOuttakePort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kNoteDetectorPort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kRightOuttakePort;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.robot.crescendo.Constants.JukeboxConstants;

public class JukeboxSubsystem extends SubsystemBase {
    //=== motors ===
	private TalonFX leftOuttakeMotor;
    private TalonFX rightOuttakeMotor;
	private TalonSRX intakeMotor;

	private final VelocityVoltage velocity = new VelocityVoltage(0);
	private final VelocityVoltage leftRequest = new VelocityVoltage(0.0);
	private final VelocityVoltage rightRequest = new VelocityVoltage(0.0);

	private ColorSensorV3 distanceSensor;

	boolean override = false;

	public JukeboxSubsystem() {
		leftOuttakeMotor = new TalonFX(kLeftOuttakePort);
		addChild("leftOuttakeMotor", leftOuttakeMotor);

		rightOuttakeMotor = new TalonFX(kRightOuttakePort);
		addChild("rightOuttakeMotor", rightOuttakeMotor);

		intakeMotor = new TalonSRX(kIntakePort);
		addChild("intakeMotor", builder -> {
			builder.addDoubleProperty("Output Voltage", intakeMotor::getMotorOutputVoltage, null);
			builder.addDoubleProperty("Output Percent", intakeMotor::getMotorOutputPercent, null);
		});
		
		MotorOutputConfigs lConfigs = new MotorOutputConfigs();
		lConfigs.Inverted = InvertedValue.CounterClockwise_Positive ;

		MotorOutputConfigs rConfigs = new MotorOutputConfigs();
		rConfigs.Inverted = InvertedValue.Clockwise_Positive;
        
        //leftOuttakeMotor.selectProfileSlot(0, 0);
        //leftOuttakeMotor.configClosedloopRamp(0.1);

		leftOuttakeMotor.getConfigurator().apply(lConfigs);
		rightOuttakeMotor.getConfigurator().apply(rConfigs);

		intakeMotor.setInverted(true);

		velocity.Slot = 0;
		
		//=== pid configs ===
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kS = JukeboxConstants.outtakeKS;
		slot0Configs.kV = JukeboxConstants.outtakeKV;
		slot0Configs.kP = JukeboxConstants.outtakeKP;
		slot0Configs.kI = JukeboxConstants.outtakeKI; 
		slot0Configs.kD = JukeboxConstants.outtakeKD; 

		leftOuttakeMotor.getConfigurator().apply(slot0Configs);
		rightOuttakeMotor.getConfigurator().apply(slot0Configs);

		distanceSensor = new ColorSensorV3(kNoteDetectorPort);
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
		leftOuttakeMotor.setControl(leftRequest.withVelocity(lSpeed * JukeboxConstants.kMaxVelocity ));
		rightOuttakeMotor.setControl(rightRequest.withVelocity(rSpeed * JukeboxConstants.kMaxVelocity));
	}

	public double getLeftVoltage(){
		return leftOuttakeMotor.getMotorVoltage().getValueAsDouble();
	}

	public double getRightVoltage(){
		return rightOuttakeMotor.getMotorVoltage().getValueAsDouble();
	}

	public void setIntakeSpeed(double speed){
		intakeMotor.set(ControlMode.PercentOutput, speed);
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
				// if (!interrupted) {
				// 	setIntakeSpeed(0);
				// 	setOuttakeSpeed(0);
				// }
			}

		});
	}

	public boolean hasNote(){
		return getDistance() > JukeboxConstants.kNoteDetectionDistance;
	}

	public double getLeftVelo(){
		return leftOuttakeMotor.getVelocity().getValueAsDouble();
	}

	public double getRightVelo(){
		return rightOuttakeMotor.getVelocity().getValueAsDouble();
	}

	public double getLeftDesiredVelo(){
		return leftRequest.Velocity;
	}

	public double getRightDesiredVelo(){
		return rightRequest.Velocity;
	}

	public double getDistance(){
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
