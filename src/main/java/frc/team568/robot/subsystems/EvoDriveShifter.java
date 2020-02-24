package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import frc.team568.robot.RobotBase;

public class EvoDriveShifter extends SubsystemBase {
	DoubleSolenoid solenoid;
	private boolean isHighGear;

	public EvoDriveShifter(final RobotBase robot) {
		super(robot);
		solenoid = new DoubleSolenoid(configInt("solenoidLow"), configInt("solenoidHigh"));
		SendableRegistry.addChild(this, solenoid);
		shiftLow();
	}

	@Override
	public String getConfigName() {
		return "shifter";
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
}