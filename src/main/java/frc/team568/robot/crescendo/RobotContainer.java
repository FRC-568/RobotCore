package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public final class RobotContainer {
	Map<String, Command> eventMap = new HashMap<>();

	public PneumaticSubsystem pneumaticsub;
	// Auto tab objects
	public AutoTab autoTab;

	public RobotContainer() {
		autoTab = new AutoTab(this);
		pneumaticsub = new PneumaticSubsystem();
		configureButtonBinding();
	}

	public void configureButtonBinding(){
		//OI.Button.pneumaticup.onTrue(new UpPneumatic(dSolenoid));
		//OI.Button.pneumaticdown.onTrue(new DownPneumatic(dSolenoid));
		OI.Button.pneumaticsubsystem.onTrue(new InstantCommand(pneumaticsub::SwitchState));
		//OI.Button.pneumaticsubsystem.onTrue(StateSwitch());
	}

	public Command getAutonomousCommand() {
		return autoTab.chooser.getSelected();
	}

	/*public Command StateSwitch(){
		return PneumaticSubsystem.SwitchState();
	} */

}
