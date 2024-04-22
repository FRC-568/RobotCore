package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Command;

public class UpPneumatic extends Command {
	DoubleSolenoid dSolenoid;

	public UpPneumatic(DoubleSolenoid dSolenoid){
		this.dSolenoid = dSolenoid;
	}

	@Override
	public void initialize() {
		dSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	@Override
	public void execute() {
		dSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
