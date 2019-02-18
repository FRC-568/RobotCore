package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.RobotBase;

public class EvoDriveShifter extends SubsystemBase {
	DoubleSolenoid solenoid;
	private boolean isHighGear;

	public EvoDriveShifter(final RobotBase robot) {
		super(robot);
		solenoid = new DoubleSolenoid(configInt("solenoidLow"), configInt("solenoidHigh"));
		addChild(solenoid);
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

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Command() {
			boolean shiftIsHeld = false;

			{ requires(EvoDriveShifter.this); }

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {
				if(button("shifterToggle")) {
					if(!shiftIsHeld)
						shiftToggle();
					shiftIsHeld = true;
				} else {
					shiftIsHeld = false;
				}		
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});
	}
}