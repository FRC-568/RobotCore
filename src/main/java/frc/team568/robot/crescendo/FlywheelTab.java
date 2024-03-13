package frc.team568.robot.crescendo;

public class FlywheelTab {
	public FlywheelTab(RobotContainer container) {
		
		var jukebox = container.jukebox;
 
		
		OI.flywheelTab.addDouble("left Flywheel", jukebox::getLeftVelo).withPosition(1,1);
		OI.flywheelTab.addDouble("right Flywheel", jukebox::getRightVelo).withPosition(3,1);

		OI.flywheelTab.addDouble("leftVoltage", jukebox::getLeftVoltage);
		OI.flywheelTab.addDouble("rightVoltage", jukebox::getRightVoltage);

		OI.flywheelTab.addDouble("sensor distance", jukebox::getDistance);
		OI.flywheelTab.addBoolean("contains note", jukebox::hasNote);
	}
}
