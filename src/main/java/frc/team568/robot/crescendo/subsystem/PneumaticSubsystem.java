package frc.team568.robot.crescendo.subsystem;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import java.util.function.BooleanSupplier;

public final class PneumaticSubsystem extends SubsystemBase {
	private final DoubleSolenoid liftSolenoid;
	private final Compressor compressor;
	private boolean compressorEnabled = true;
	private BooleanSupplier interrupter;

	public PneumaticSubsystem() {
		compressor = new Compressor(PneumaticsModuleType.CTREPCM);
		liftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 7);
		retractLift();
	}

	public void extendLift() {
		liftSolenoid.set(kForward);
	}

	public void retractLift() {
		liftSolenoid.set(kReverse);
	}

	public void toggleLift() {
		liftSolenoid.toggle();
	}

	public Command getExtendCommand() {
		return new InstantCommand(this::extendLift, this);
	}

	public Command getRetractCommand() {
		return new InstantCommand(this::retractLift, this);
	}

	public Command getToggleCommand() {
		return new InstantCommand(this::toggleLift, this);
	}

	public boolean isAtPressure() {
		return !compressor.getPressureSwitchValue();
	}

	public boolean isCompressorEnabled() {
		return compressor.isEnabled();
	}

	public boolean isInterrupted() {
		return compressorEnabled && !compressor.isEnabled();
	}

	public void disableCompressor() {
		compressorEnabled = false;
		compressor.disable();
	}

	public void enableCompressor() {
		compressorEnabled = true;
		compressor.enableDigital();
	}

	public double getCompressorCurrent() {
		return compressor.getCurrent();
	}

	public void setInterupter(BooleanSupplier interrupter) {
		this.interrupter = interrupter;
	}

	@Override
	public void periodic() {
		if (compressorEnabled && interrupter != null) {
			boolean interruptNeeded = interrupter.getAsBoolean();
			boolean compressorActive = compressor.isEnabled();

			if (interruptNeeded && compressorActive)
				compressor.disable();
			else if (!interruptNeeded && !compressorActive){
				compressor.enableDigital();
			}
		}
	}
}
