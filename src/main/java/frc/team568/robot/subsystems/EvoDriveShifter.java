package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;

public class EvoDriveShifter extends SubsystemBase {
	DoubleSolenoid solenoid;
	private boolean isHighGear;

	public EvoDriveShifter(final RobotBase robot) {
		super(robot);
		solenoid = new DoubleSolenoid(configInt("solenoidLow"), configInt("solenoidHigh"));
		SendableRegistry.addChild(this, solenoid);
		shiftLow();
		initDefaultCommand();
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

	public void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {
			boolean shiftIsHeld = false;

			{
				addRequirements(EvoDriveShifter.this);
				SendableRegistry.addChild(EvoDriveShifter.this, this);
			}

			@Override
			public void execute() {
				if(button("shifterToggle")) {
					if(!shiftIsHeld)
						shiftToggle();
					shiftIsHeld = true;
				} else {
					shiftIsHeld = false;
				}		
			}
			
		});
	}
}