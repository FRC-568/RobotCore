package frc.team568.robot.crescendo;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Command;

public class DownPneumatic extends Command {
	DoubleSolenoid dSolenoid;
	
	public DownPneumatic(DoubleSolenoid dSolenoid){
		this.dSolenoid = dSolenoid;
	}

	@Override
	public void initialize() {
		dSolenoid.set(DoubleSolenoid.Value.kOff);
	}

	@Override
	public void execute() {
		dSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
	
}
