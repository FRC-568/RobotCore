package frc.team568.robot.crescendo;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class TuningAndConfigs{

	//Tuning tab for error thresholds and PID (maybe)
	ShuffleboardTab Tuning = Shuffleboard.getTab("Tuning");
	static GenericEntry totalerrorThreshold = Shuffleboard.getTab("Tuning")
		.add("Total Error Threshold", 0.75)
		.withWidget(BuiltInWidgets.kNumberSlider)
		.withProperties(Map.of("min",0,"max",1))
		.getEntry();
	static GenericEntry spikeerrorThreshold = Shuffleboard.getTab("Tuning")
		.add("Spike Error Threshold", 1)
		.withWidget(BuiltInWidgets.kNumberSlider)
		.withProperties(Map.of("min",0,"max",1))
		.getEntry();
	// Wheel rotation tuning just so that it may be easier to see if the changes actually occur live
	static GenericEntry wheelrotation = Shuffleboard.getTab("Tuning")
		.add("Test thing (probably wheel rotation)", 24)
		.withWidget(BuiltInWidgets.kNumberSlider)
		.withProperties(Map.of("min",0,"max",360))
		.getEntry();

}