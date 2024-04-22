package frc.team568.robot.crescendo;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;

public class ConfigTab {
	
	public GenericEntry kpEntry;
	public GenericEntry kiEntry;
	public GenericEntry kdEntry;
	
	public ConfigTab(RobotContainer container) {
		// kpEntry = OI.configTab.add("kp", 0)
		// .withProperties(Map.of("min", 0, "max", 1))
		// .withPosition(0, 0)
		// .withSize(1, 1)
		// .getEntry();
		
		// kiEntry = OI.configTab.add("ki",0)
		// .withProperties(Map.of("min", 0, "max", 1))
		// .withPosition(1, 0)
		// .withSize(1, 1)
		// .getEntry();
							
		// kdEntry = OI.configTab.add("kd", 0)
		// .withProperties(Map.of("min", 0, "max", 1))
		// .withPosition(2, 0)
		// .withSize(1, 1)
		// .getEntry();
	}
}
