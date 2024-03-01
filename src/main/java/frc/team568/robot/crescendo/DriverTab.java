package frc.team568.robot.crescendo;

public class DriverTab {
	public DriverTab(RobotContainer container) {
		
		var drive = container.drive;
		
		OI.driverTab.addDouble("Travel Velocity", drive::getTravelSpeedMS).withSize(1, 1).withPosition(0, 0);
		OI.driverTab.addDouble("Travel Direction", drive::getTravelBearingDeg).withSize(1, 1).withPosition(0, 0);
		OI.driverTab.addDouble("Travel Facing", drive::getHeadingDeg).withSize(1, 1).withPosition(4, 0);
		//		OI.driverTab.addDouble("left Flywheel", jukebox::getLeftVelo)

	}
}
