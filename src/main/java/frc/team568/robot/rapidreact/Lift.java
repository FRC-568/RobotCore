package frc.team568.robot.rapidreact;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.CTREPCM;
import static frc.team568.robot.rapidreact.Config.Lift.kMotorId;
import static frc.team568.robot.rapidreact.Config.Lift.kSlantedFlow;
import static frc.team568.robot.rapidreact.Config.Lift.kUprightFlow;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.util.Utilities;

class Lift extends SubsystemBase {
	DoubleSolenoid liftSolenoid;
	WPI_TalonSRX liftMotor;

	public Lift() {
		// Upright = forward, slanted = reverse
		liftSolenoid = new DoubleSolenoid(CTREPCM, kUprightFlow, kSlantedFlow);

		liftMotor = new WPI_TalonSRX(kMotorId);
		liftMotor.setNeutralMode(NeutralMode.Brake);
	}
	
	void setUpright(boolean upright) {	
		liftSolenoid.set(upright ? Value.kForward : Value.kReverse);
	}

	boolean isUpright() {
		return liftSolenoid.get() == Value.kForward;
	}

	void toggle() {
		setUpright(!isUpright());
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
		builder.addBooleanProperty("Lift Upright", this::isUpright, null);
	}
}
