package frc.team568.robot.crescendo.subsystem;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PivotSubsystem extends SubsystemBase {
    //=== motors ===
	private TalonFX leftMotor;
	private CANcoder leftMotorcCaNcoder;
    private TalonFX rightMotor;

	boolean override = false;

	public PivotSubsystem(int leftMotorPort, int rightMotorPort) {
		leftMotor = new TalonFX(leftMotorPort);
		leftMotorcCaNcoder = new CANcoder(leftMotorPort);
		addChild("leftMotor", leftMotor);

		rightMotor = new TalonFX(rightMotorPort);
		addChild("rightMotor", rightMotor);

		MotorOutputConfigs currentConfigs = new MotorOutputConfigs();
		currentConfigs.Inverted = InvertedValue.Clockwise_Positive; //TODO: reverse directions based on design

		leftMotor.getConfigurator().apply(currentConfigs);
		rightMotor.setControl(new Follower(leftMotor.getDeviceID(), true));

		//manning
		//rightMotor.optimizeBusUtilization()
		
		
		//=== pid configs ===
		//TODO: allow on the fly configuration
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = 0; //An error of 0.5 rotations and a value of 24 results in 12 V output
		slot0Configs.kI = 0; //no output for integrated error
		slot0Configs.kD = 0; //A velocity of 1 rps results in 0.1 V output at a setting of 0.1

		leftMotor.getConfigurator().apply(slot0Configs);
        
	}

	/**
	 * 
	 * @param angle degrees from ... somewhere
	 */
	//TODO: figure out where 0 is
    public void setAngle(double angle){
		angle /= 360.0; //degrees to rotations
		final PositionVoltage request = new PositionVoltage(0).withSlot(0);

		//set position to 10 rotations
		leftMotor.setControl(request.withPosition(angle));
	}

	public boolean getAngle(){
		return false;
	}

	
	public void populate(double kP, double kI, double kD){
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = kP;
		slot0Configs.kI = kI;
		slot0Configs.kD = kD;

		leftMotor.getConfigurator().apply(slot0Configs);
	}


	public void track(double distance){
		double h = 12.34; //speaker height
		double d = distance;
		double a = Math.sqrt(d * d + h * h);
		double c = 5.67; //arm length
		double alpha = 60.0; //angle between arm and jukebox 

		double theta = Math.atan(h / d) + 180 - alpha - Math.asin( (c / a) * Math.sin(alpha) );

		setAngle( Math.toDegrees(theta) );
	}


	public void setDef(){
		Slot0Configs slot0Configs = new Slot0Configs();
		slot0Configs.kP = 0;
		slot0Configs.kI = 0;
		slot0Configs.kD = 0;

		leftMotor.getConfigurator().apply(slot0Configs);
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
