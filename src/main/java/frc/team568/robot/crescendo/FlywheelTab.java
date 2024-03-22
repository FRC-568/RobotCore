package frc.team568.robot.crescendo;

public class FlywheelTab {
	public FlywheelTab(RobotContainer container) {
		
		var jukebox = container.jukebox;
		var pivot = container.pivot;
		var lift = container.lift;
 
		
		// OI.flywheelTab.addDouble("left vel", jukebox::getLeftVelo).withPosition(1,1);
		// OI.flywheelTab.addDouble("right vel", jukebox::getRightVelo).withPosition(3,1);
		// OI.flywheelTab.addDouble("left desired vel", jukebox::getLeftDesiredVelo);
		// OI.flywheelTab.addDouble("right desired vel", jukebox::getRightDesiredVelo);

		// OI.flywheelTab.addDouble("leftVoltage", jukebox::getLeftVoltage);
		// OI.flywheelTab.addDouble("rightVoltage", jukebox::getRightVoltage);

		// OI.flywheelTab.addDouble("sensor distance", jukebox::getDistance);
		OI.flywheelTab.addBoolean("contains note", jukebox::hasNote);
		OI.flywheelTab.addBoolean("compressor interupted", lift::isInterrupted);

		OI.flywheelTab.addDouble("pivot angle", pivot::getAngle);
		//OI.flywheelTab.addDouble("pivot angle", pivot.get);
		// OI.flywheelTab.addDouble("pivot deg per s", pivot::getVelocity);
		// OI.flywheelTab.addDouble("pivot ref angle", pivot::getClosedLoopReference);
		// OI.flywheelTab.addDouble("pivot ref deg per s", pivot::getClosedLoopReferenceSlope);

		// OI.flywheelTab.addDouble("pivot voltage", pivot::getVoltage);
		// OI.flywheelTab.addDouble("kv ig?", () -> pivot.getVelocity()/pivot.getVoltage());
	}
}
