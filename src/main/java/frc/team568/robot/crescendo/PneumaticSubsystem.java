package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Command;

public class PneumaticSubsystem extends Command {

	DoubleSolenoid dSolenoid;
	Compressor compressor;

	public PneumaticSubsystem(DoubleSolenoid dSolenoid, Compressor compressor){
		this.dSolenoid = dSolenoid;
		this.compressor = compressor;
	} 

	@Override
	public void initialize(){
		dSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	public void SwitchState(){
		dSolenoid.toggle();
		System.out.print("THIS IS WORKING 2");
	} 

	{
	if (OI.Button.switchstate != null){
		SwitchState();
		System.out.print("THIS IS WORKING (maybe)");
	}

	
	}
/* 
	public void Up(){
		dSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public void Down(){
		dSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	*/

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
