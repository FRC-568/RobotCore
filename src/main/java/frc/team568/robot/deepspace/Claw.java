package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Claw extends SubsystemBase {
	private DoubleSolenoid solenoid;

	Claw(RobotBase robot) {
		super(robot);

		solenoid = new DoubleSolenoid(configInt("solenoidOpen"), configInt("solenoidClose"));
		setDefaultCommand(new RunCommand(() -> {
			holdToToggle(button("clawToggle"));

			if (button("clawOpen"))
				setOpen(true);

			if (button("clawClose"))
				setOpen(false);
		}, this));
	}

	@Override
	public String getConfigName() {
		return "claw";
	}

	void setOpen(boolean open) {
		if (open)
			solenoid.set(Value.kForward);
		else
			solenoid.set(Value.kReverse);
	}

	boolean getOpen() {
		return solenoid.get() == Value.kForward;
	}

	void toggleOpen() {
		setOpen(!getOpen());
	}

	private boolean _holdtoToggle = false;
	private void holdToToggle(boolean button) {
		if (button) {
			if (!_holdtoToggle)
				toggleOpen();
			_holdtoToggle = true;
		} else {
			_holdtoToggle = false;
		}
	}

}