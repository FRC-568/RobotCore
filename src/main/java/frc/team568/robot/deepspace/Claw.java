package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

class Claw extends SubsystemBase {
	private DoubleSolenoid solenoid;

	Claw(RobotBase robot) {
		super(robot);

		solenoid = new DoubleSolenoid(configInt("solenoidOpen"), configInt("solenoidClose"));
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

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new Command() {
			boolean toggleIsHeld = false;

			{ requires(Claw.this); }

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {
				if(button("clawToggle")) {
					if(!toggleIsHeld)
						toggleOpen();
					toggleIsHeld = true;
				} else {
					toggleIsHeld = false;
				}
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});
	}

}