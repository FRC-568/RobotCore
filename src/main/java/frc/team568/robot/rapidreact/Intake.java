package frc.team568.robot.rapidreact;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.CTREPCM;
import static frc.team568.robot.rapidreact.Config.Intake.kLidClosed;
import static frc.team568.robot.rapidreact.Config.Intake.kLidOpen;
import static frc.team568.robot.rapidreact.Config.Intake.kLiftDown;
import static frc.team568.robot.rapidreact.Config.Intake.kLiftUp;
import static frc.team568.robot.rapidreact.Config.Intake.kMotorId;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team568.util.Utilities;

class Intake extends SubsystemBase {
	DoubleSolenoid intakeLift, intakeLid;
	CANSparkMax intakeMotor;

	Intake() {
		// Down = forward, Up = reverse
		intakeLift = new DoubleSolenoid(CTREPCM, kLiftDown, kLiftUp);
		// Open = forward, Closed = reverse
		intakeLid = new DoubleSolenoid(CTREPCM, kLidOpen, kLidClosed);

		intakeMotor = new CANSparkMax(kMotorId, MotorType.kBrushed);
	}

	boolean isLidOpen() {
		return intakeLid.get() == Value.kForward;
	}

	void setLidOpen(boolean open) {
		intakeLid.set(open ? Value.kForward : Value.kReverse);
	}

	void toggleLid() {
		setLidOpen(!isLidOpen());
	}

	boolean isLiftUp() {
		return intakeLift.get() == Value.kReverse;
	}

	void setLiftUp(boolean up) {
		intakeLift.set(up ? Value.kReverse : Value.kForward);
	}

	void toggleLift() {
		setLiftUp(!isLiftUp());
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

	Command commandOpenLid() {
		return new InstantCommand(() -> setLidOpen(true));
	}

	Command commandCloseLid() {
		return new InstantCommand(() -> setLidOpen(false));
	}

	Command commandToggleLid() {
		return new InstantCommand(this::toggleLid);
	}

	Command commandLowerLift() {
		return new InstantCommand(() -> setLiftUp(false));
	}

	void stop() {
		intakeMotor.set(0.0);
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Intake Lift Up", this::isLiftUp, null);
		builder.addBooleanProperty("Lid Open", this::isLidOpen, null);
	}
}
