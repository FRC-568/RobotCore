package frc.team568.robot.rapidreact;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class AutonomousParameters {
	AutonomousParameters() {
		setupShuffleboard();
	}

	ShuffleboardTab tab;
	NetworkTableEntry taxiTimeout;
	NetworkTableEntry lungeTime;
	NetworkTableEntry intakeTimeout;
	NetworkTableEntry lidDelay;

	BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();

	private void setupShuffleboard() {
		tab = Shuffleboard.getTab("Parameters");

		taxiTimeout = tab.add("Taxi Timeout", 1.5)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 4)).getEntry();
		taxiTimeout.setPersistent();

		lungeTime = tab.add("Lunge Timeout", 1)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		lungeTime.setPersistent();

		lidDelay = tab.add("Lid Closing Delay", 0.3)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		lidDelay.setPersistent();

		tab.addNumber("Accel getZ", accelerometer::getZ);
	}
}