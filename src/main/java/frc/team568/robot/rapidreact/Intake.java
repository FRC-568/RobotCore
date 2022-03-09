package frc.team568.robot.rapidreact;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.util.Utilities;

public class Intake extends SubsystemBase {
	DoubleSolenoid intakeLift, intakeLid;
	CANSparkMax intakeMotor;

	Intake(int intakeLiftUp, int intakeLiftDown, int intakeLidOpen, int intakeLidClosed, int motorID){
		// Down = forward, Up = reverse
		intakeLift = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, intakeLiftDown, intakeLiftUp);
		// Open = forward, Closed = reverse
		intakeLid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, intakeLidOpen, intakeLidClosed);

		intakeMotor = new CANSparkMax(motorID, MotorType.kBrushed);
	}

	boolean getLidOpen() {
		return intakeLid.get() == Value.kForward;
	}

	void setLidOpen(boolean open) {
		intakeLid.set(open ? Value.kForward : Value.kReverse);
	}

	void toggleLid() {
		setLidOpen(!getLidOpen());
	}

	boolean getLiftUp() {
		return intakeLift.get() == Value.kReverse;
	}

	void setLiftUp(boolean up) {
		intakeLift.set(up ? Value.kReverse : Value.kForward);
	}

	void toggleLift() {
		setLiftUp(!getLiftUp());
	}

	void setIntakeMotor(double speed){
		intakeMotor.set(Utilities.clamp(speed, -1, 1));
	}

	DoubleSolenoid getIntakeLift() {
		return intakeLift;
	}

	DoubleSolenoid getIntakeLid() {
		return intakeLid;
	}

	CANSparkMax getIntakeMotor() {
		return intakeMotor;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Lid Open", this::getLidOpen, null);
		builder.addBooleanProperty("Intake Lift Up", this::getLiftUp, null);
	}
}
