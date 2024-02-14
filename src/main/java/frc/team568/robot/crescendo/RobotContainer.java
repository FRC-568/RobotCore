package frc.team568.robot.crescendo;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj2.command.Command;
//import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
//import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public final class RobotContainer {
	Map<String, Command> eventMap = new HashMap<>();

	// Auto tab objects
	public AutoTab autoTab;

	public DoubleSolenoid dSolenoid = new DoubleSolenoid(null, 0, 0);

	public RobotContainer() {
		autoTab = new AutoTab(this);
	}

	public Command getAutonomousCommand() {
		return autoTab.chooser.getSelected();
	}
}
