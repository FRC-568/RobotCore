package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj2.command.Command;

public final class RobotContainer {
	Map<String, Command> eventMap = new HashMap<>();

	// Auto tab objects
	public AutoTab autoTab;

	//Need to put in module type and channels for dSolenoids, but other than that everything should be good?
	public DoubleSolenoid dSolenoid = new DoubleSolenoid(null, 0, 0);
	public Compressor compressor = new Compressor(null);

	public RobotContainer() {
		autoTab = new AutoTab(this);
	}

	public void configureButtonBinding(){
		//OI.Button.pneumaticup.onTrue(new UpPneumatic(dSolenoid));
		//OI.Button.pneumaticdown.onTrue(new DownPneumatic(dSolenoid));
		OI.Button.pneumaticsubsystem.onTrue(new PneumaticSubsystem(dSolenoid, compressor));
	}

	public Command getAutonomousCommand() {
		return autoTab.chooser.getSelected();
	}

}
