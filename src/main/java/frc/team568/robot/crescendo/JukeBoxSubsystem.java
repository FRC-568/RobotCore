package frc.team568.robot.crescendo;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class JukeBoxSubsystem extends SubsystemBase {
    //=== motors ===
	private TalonFX leftOuttakeMotor;
    private TalonFX rightOuttakeMotor;
	private CANSparkMax intakeMotor;


	boolean override = false;

	public JukeBoxSubsystem(int leftOuttakeMotorPort, int rightOuttakeMotorPort, int intakeMotorPort) {
		leftOuttakeMotor = new TalonFX(leftOuttakeMotorPort);
		addChild("leftOuttakeMotor", leftOuttakeMotor);

		rightOuttakeMotor = new TalonFX(rightOuttakeMotorPort);
		addChild("rightOuttakeMotor", rightOuttakeMotor);

		
		intakeMotor = new CANSparkMax(intakeMotorPort, MotorType.kBrushless);
		//addChild("intakeMotor", intakeMotor);
		//TODO: thats important
		
		MotorOutputConfigs lConfigs = new MotorOutputConfigs();
		lConfigs.Inverted = InvertedValue.Clockwise_Positive; //TODO: reverse directions based on design

		MotorOutputConfigs rConfigs = new MotorOutputConfigs();
		rConfigs.Inverted = InvertedValue.Clockwise_Positive;


        
        //leftOuttakeMotor.selectProfileSlot(0, 0);
        //leftOuttakeMotor.configClosedloopRamp(0.1);

		leftOuttakeMotor.getConfigurator().apply(lConfigs);
		rightOuttakeMotor.getConfigurator().apply(rConfigs);

		intakeMotor.setInverted(true);

		//TODO: Make invert intake motor based on design

		//manning
		//rightOuttakeMotor.optimizeBusUtilization()
		
		
		//=== pid configs ===
		//TODO: allow on the fly configuration
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = 0; //An error of 0.5 rotations and a value of 24 results in 12 V output
		slot0Configs.kI = 0; //no output for integrated error
		slot0Configs.kD = 0; //A velocity of 1 rps results in 0.1 V output at a setting of 0.1

		leftOuttakeMotor.getConfigurator().apply(slot0Configs);
        
	}

	public void setOuttakeSpeed(double lSpeed, double rSpeed) {

			leftOuttakeMotor.setVoltage(lSpeed);
			rightOuttakeMotor.setVoltage(rSpeed);


	}

	public void setIntakeSpeed(double speed){
		intakeMotor.set(speed);
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
