package frc.team568.robot.deepspace;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class Claw extends SubsystemBase {
	private final DoubleSolenoid solenoid;

	Claw(final int openPort, final int closePort) {
		solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, openPort, closePort);
		addChild("Solenoid", solenoid);
	}

	void setOpen(boolean open) {
		solenoid.set(open ? Value.kForward : Value.kReverse);
	}

	boolean getOpen() {
		return solenoid.get() == Value.kForward;
	}

	void toggleOpen() {
		setOpen(!getOpen());
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Open", this::getOpen, this::setOpen);
	}

}