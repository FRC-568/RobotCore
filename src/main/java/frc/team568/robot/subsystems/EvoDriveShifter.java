package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.util.sendable.SendableBuilder;

public class EvoDriveShifter extends SubsystemBase {
	final DoubleSolenoid solenoid;
	private boolean isHighGear;

	public EvoDriveShifter(final int lowSolenoidPort, final int highSolenoidPort) {
		solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, lowSolenoidPort, highSolenoidPort);
		addChild("Solenoid", solenoid);
		shiftLow();
	}

	public void shiftLow() {
		solenoid.set(Value.kForward);
		isHighGear = false;
	}

	public void shiftHigh() {
		solenoid.set(Value.kReverse);
		isHighGear = true;
	}

	public boolean shiftToggle() {
		if(isHighGear)
			shiftLow();
		else
			shiftHigh();
		return isHighGear;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("High Gear", () -> isHighGear, state -> isHighGear = state);
	}
}