package frc.team568.robot.deepspace;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Shpaa extends SubsystemBase {
	private DoubleSolenoid extender;
	private DoubleSolenoid grabber;

	Shpaa(RobotBase robot) {
		super(robot);

		extender = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, configInt("extenderOut"), configInt("extenderIn"));
		grabber = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, configInt("grabberOpen"), configInt("grabberClose"));
		initDefaultCommand();
	}

	@Override
	public String getConfigName() {
		return "shpaa";
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

	public void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {
			boolean grabberIsHeld = false;
			boolean extenderIsHeld = false;

			{
				addRequirements(Shpaa.this);
				SendableRegistry.addChild(Shpaa.this, this);
			}

			@Override
			public void execute() {
				if (button("shpaaGrabberToggle")) {
					if (!grabberIsHeld)
						toggleGrabber();
					grabberIsHeld = true;
				} else {
					grabberIsHeld = false;
				}

				if (button("shpaaGrabberOpen"))
					setGrabberOpen(true);

				if (button("shpaaGrabberClose"))
					setGrabberOpen(false);

				if (button("shpaaExtenderToggle")) {
					if (!extenderIsHeld)
						toggleExtender();
					extenderIsHeld = true;
				} else {
					extenderIsHeld = false;
				}

				if (button("shpaaExtenderOut"))
					setExtenderOut(true);

				if (button("shpaaExtenderIn"))
					setExtenderOut(false);
			}
			
		});
	}

}