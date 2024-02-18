package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;

public class UpPneumatic extends Command {
	public DoubleSolenoid dSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

	public UpPneumatic(){
	}

	@Override
	public void initialize() {
		dSolenoid.set(DoubleSolenoid.Value.kOff);
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
