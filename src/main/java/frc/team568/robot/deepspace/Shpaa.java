package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Shpaa extends SubsystemBase {
	private DoubleSolenoid extender;
	private DoubleSolenoid grabber;

	Shpaa(RobotBase robot) {
		super(robot);

		extender = new DoubleSolenoid(configInt("extenderOut"), configInt("extenderIn"));
		grabber = new DoubleSolenoid(configInt("grabberOpen"), configInt("grabberClose"));
	}

	@Override
	public String getConfigName() {
		return "shpaa";
	}

	void setExtenderOut(boolean out) {
		if (out)
			extender.set(Value.kForward);
		else
			extender.set(Value.kReverse);
	}

	boolean getExtenderOut() {
		return extender.get() == Value.kForward;
	}

	void toggleExtender() {
		setExtenderOut(!getExtenderOut());
	}

	void setGrabberOpen(boolean open) {
		if (open)
			grabber.set(Value.kForward);
		else
			grabber.set(Value.kReverse);
	}

	boolean getGrabberOpen() {
		return grabber.get() == Value.kForward;
	}

	void toggleGrabber() {
		setGrabberOpen(!getGrabberOpen());
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Command() {
			boolean grabberIsHeld = false;
			boolean extenderIsHeld = false;

			{ requires(Shpaa.this); }

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {
				if(button("shpaaGrabberToggle")) {
					if(!grabberIsHeld)
						toggleGrabber();
					grabberIsHeld = true;
				} else {
					grabberIsHeld = false;
				}

				if(button("shpaaExtenderToggle")) {
					if(!extenderIsHeld)
						toggleExtender();
					extenderIsHeld = true;
				} else {
					extenderIsHeld = false;
				}	
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});
	}

}