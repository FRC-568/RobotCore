package frc.team568.robot.deepspace;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
class Shpaa extends SubsystemBase {
	private final DoubleSolenoid extender;
	private final DoubleSolenoid grabber;

	Shpaa(final int extenderOutPort,
			final int extenderInPort,
			final int grabberOpenPort,
			final int grabberClosePort) {
		extender = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, extenderOutPort, extenderInPort);
		grabber = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, grabberOpenPort, grabberClosePort);
		addChild("Extender Solenoid", extender);
		addChild("Grabber Solenoid", grabber);
	}

	void setExtenderOut(boolean out) {
		extender.set(out ? Value.kForward : Value.kReverse);
	}

	boolean getExtenderOut() {
		return extender.get() == Value.kForward;
	}

	void toggleExtender() {
		setExtenderOut(!getExtenderOut());
	}

	void setGrabberOpen(boolean open) {
		grabber.set(open ? Value.kForward : Value.kReverse);
	}

	boolean getGrabberOpen() {
		return grabber.get() == Value.kForward;
	}

	void toggleGrabber() {
		setGrabberOpen(!getGrabberOpen());
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Extender Out", this::getExtenderOut, this::setExtenderOut);
		builder.addBooleanProperty("Grabber Open", this::getGrabberOpen, this::setGrabberOpen);
	}

}