package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;

public class PneumaticSubsystem extends Command {
  	private final DoubleSolenoid dSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 1, 2);
	private final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);
	
	public void Forward(){
		dSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public void Reverse(){
		dSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	public double getPressure(){
		return compressor.getPressure();
	}

	public double getCurrent(){
		return compressor.getCurrent();
	}

	public boolean isEnabled(){
		return compressor.isEnabled();
	}

	public boolean getPressureSwitchValue(){
		return compressor.getPressureSwitchValue();
	}

	public void clDisable(){
		compressor.disable();
	}

	public void clEnable(){
		compressor.enableDigital();
	}

	@Override
	public void initialize() {
		dSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		dSolenoid.set(DoubleSolenoid.Value.kOff);
	}
}
