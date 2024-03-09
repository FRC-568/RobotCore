package frc.team568.robot.crescendo.subsystem;

import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kIntakePort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kLeftOuttakePort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kNoteDetectorPort;
import static frc.team568.robot.crescendo.Constants.JukeboxConstants.kRightOuttakePort;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class JukeboxSubsystem extends SubsystemBase {
    //=== motors ===
	private TalonFX leftOuttakeMotor;
    private TalonFX rightOuttakeMotor;
	private VictorSPX intakeMotor;

	private final VelocityVoltage velocity = new VelocityVoltage(0);
	private final DutyCycleOut leftRequest = new DutyCycleOut(0.0);
	private final DutyCycleOut rightRequest = new DutyCycleOut(0.0);

	private ColorSensorV3 distanceSensor;

	boolean override = false;

	public JukeboxSubsystem() {
		leftOuttakeMotor = new TalonFX(kLeftOuttakePort);
		addChild("leftOuttakeMotor", leftOuttakeMotor);

		rightOuttakeMotor = new TalonFX(kRightOuttakePort);
		addChild("rightOuttakeMotor", rightOuttakeMotor);

		intakeMotor = new VictorSPX(kIntakePort);
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
		slot0Configs.kP = 0.5; //An error of 0.5 rotations and a value of 24 results in 12 V output
		slot0Configs.kI = 0; //no output for integrated error
		slot0Configs.kD = 0; //A velocity of 1 rps results in 0.1 V output at a setting of 0.1

		distanceSensor = new ColorSensorV3(kNoteDetectorPort);

	//	leftOuttakeMotor.getConfigurator().apply(slot0Configs);
	//	rightOuttakeMotor.getConfigurator().apply(slot0Configs);
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
		leftOuttakeMotor.setControl(leftRequest.withOutput(lSpeed));
		rightOuttakeMotor.setControl(rightRequest.withOutput(rSpeed));
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
				setIntakeSpeed(0);
				setOuttakeSpeed(0);
			}

		});
	}

	public boolean hasNote(){
		double value = getDistance();
		double distanse = 350;
		
		if(value>distanse){
			return true;
		}
		return false;
	}

	public double getLeftVelo(){
		return leftOuttakeMotor.getVelocity().getValueAsDouble();
	}

	public double getRightVelo(){
		return rightOuttakeMotor.getVelocity().getValueAsDouble();
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
		//builder.addDoubleProperty("Stage position", () -> getStagePos(), null);
	}
}
