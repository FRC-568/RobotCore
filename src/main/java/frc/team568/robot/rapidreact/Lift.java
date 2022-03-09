package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.util.Utilities;

public class Lift extends SubsystemBase {
	DoubleSolenoid liftSolenoid;
	WPI_TalonSRX liftMotor;
	public Lift(int uprightFLow, int slantedFlow, int motorID) {
		// Upright = forward, slated = reverse
		liftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, uprightFLow, slantedFlow);

		liftMotor = new WPI_TalonSRX(motorID);
		liftMotor.setNeutralMode(NeutralMode.Brake);
	}
	
	void setUpright(boolean upright) {	
		liftSolenoid.set(upright ? Value.kForward : Value.kReverse);
	}

	boolean getUpright() {
		return liftSolenoid.get() == Value.kForward;
	}

	void toggle() {
		setUpright(!getUpright());
	}

	void setMotor(double speed){
		liftMotor.set(Utilities.clamp(speed, -1, 1));
	}

	WPI_TalonSRX getMotor() {
		return liftMotor;
	}

	DoubleSolenoid getSolenoid() {
		return liftSolenoid;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Lift Upright", this::getUpright, null);
	}
}
