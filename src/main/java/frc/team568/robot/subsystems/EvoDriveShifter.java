package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team568.robot.RobotBase;

public class EvoDriveShifter extends SubsystemBase {
	DoubleSolenoid solenoid;

	public EvoDriveShifter(final RobotBase robot) {
		super(robot);
		solenoid = new DoubleSolenoid(configInt("portForward"), configInt("portReverse"));
		addChild(solenoid);
	}

	@Override
	public String getConfigName() {
		return "shifter";
	}

	public void shiftLow() {
		solenoid.set(Value.kForward);
	}

	public void shiftHigh() {
		solenoid.set(Value.kReverse);
	}
}