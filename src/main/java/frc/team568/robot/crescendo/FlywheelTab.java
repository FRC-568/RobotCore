package frc.team568.robot.crescendo;

public class FlywheelTab {
	public FlywheelTab(RobotContainer container) {
		
		var jukebox = container.jukebox;
 
		
		OI.flywheelTab.addDouble("left vel", jukebox::getLeftVelo).withPosition(1,1);
		OI.flywheelTab.addDouble("right vel", jukebox::getRightVelo).withPosition(3,1);
		OI.flywheelTab.addDouble("left desired vel", jukebox::getLeftDesiredVelo);
		OI.flywheelTab.addDouble("right desired vel", jukebox::getRightDesiredVelo);

		OI.flywheelTab.addDouble("leftVoltage", jukebox::getLeftVoltage);
		OI.flywheelTab.addDouble("rightVoltage", jukebox::getRightVoltage);

		OI.flywheelTab.addDouble("sensor distance", jukebox::getDistance);
		OI.flywheelTab.addBoolean("contains note", jukebox::hasNote);
	}
}
