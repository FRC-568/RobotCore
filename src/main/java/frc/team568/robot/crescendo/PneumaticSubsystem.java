package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticSubsystem extends SubsystemBase {

	//public DoubleSolenoid dSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
	public Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);
	public static DoubleSolenoid dSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

	public PneumaticSubsystem(){
		dSolenoid.set(DoubleSolenoid.Value.kReverse);
		System.out.print("I STARTEDDDDDDDDDDD");
	} 

	public void SwitchState(){
		dSolenoid.toggle();
		System.out.print("IM HEREERERERERER");
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
	
}
