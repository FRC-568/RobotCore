package frc.team568.robot.crescendo;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShuffleboardTesting{

	private GenericEntry totalerrorThreshold;
	private GenericEntry spikeerrorThreshold;
	private GenericEntry wheelrotation;

	ShuffleboardTab tab = Shuffleboard.getTab("Tuning");

		//Making shuffeboard tab to (hopefully) change the tunings of the error thresholds as the robot runs
		public void createshuffletab(){
			Shuffleboard.selectTab("Tuning");
	
			totalerrorThreshold = Shuffleboard.getTab("Tuning")
				.add("Total Error Threshold", 0.75)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min",0,"max",1))
				.getEntry();
			spikeerrorThreshold = Shuffleboard.getTab("Tuning")
				.add("Spike Error Threshold", 1)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min",0,"max",1))
				.getEntry();
			// Wheel rotation tuning just so that it may be easier to see if the changes actually occur live
			wheelrotation = Shuffleboard.getTab("Tuning")
				.add("Test thing (probably wheel rotation)", 24)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("min",0,"max",360))
				.getEntry();
		}
	
	public double spikeerrorthresh(){
		double seT = spikeerrorThreshold.getDouble(1);
		return seT;
	}
	public double toterrorthresh(){
		double teT = totalerrorThreshold.getDouble(1);
		return teT;
	}
	public double wheelrot(){
		double wR = wheelrotation.getDouble(1);
		return wR;
	}



}