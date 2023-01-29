package frc.team568.robot.rapidreact;

import static edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets.kNumberSlider;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

class AutonomousParameters {
	static final double TAXI_TIME_DEF = 1.5;
	static final double TAXI_DELAY_DEF = 10.0;
	static final double CHARGE_TIME_DEF = 1.0;
	static final double SHOOT_TIME_DEF = 3.0;
	static final double LID_TIME_DEF = 0.3;

	ShuffleboardTab tab;
	GenericEntry taxiTimeEntry;
	GenericEntry taxiDelayEntry;
	GenericEntry chargeTimeEntry;
	GenericEntry shootTimeEntry;
	GenericEntry lidTimeEntry;
	GenericEntry accelTrigger;

	AutonomousParameters() {
		setupShuffleboard();
	}

	void setupShuffleboard() {
		tab = Shuffleboard.getTab("Parameters");

		taxiTimeEntry = tab.add("Taxi Time", TAXI_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 4)).getEntry();
		taxiTimeEntry.getTopic().setPersistent(true);

		taxiDelayEntry = tab.add("Taxi After", TAXI_DELAY_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0, "max", 15)).getEntry();
		taxiDelayEntry.getTopic().setPersistent(true);

		chargeTimeEntry = tab.add("Lunge Timeout", CHARGE_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		chargeTimeEntry.getTopic().setPersistent(true);

		shootTimeEntry = tab.add("Intake Timeout", SHOOT_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.05, "max", 1)).getEntry();
		shootTimeEntry.getTopic().setPersistent(true);

		lidTimeEntry = tab.add("Lid Closing Delay", LID_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		lidTimeEntry.getTopic().setPersistent(true);

		accelTrigger = tab.add("Acceleromter Gs", LID_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		accelTrigger.getTopic().setPersistent(true);
	}

	double taxiTime() {
		return taxiTimeEntry.getDouble(TAXI_TIME_DEF);
	}

	double taxiDelay() {
		return taxiDelayEntry.getDouble(TAXI_DELAY_DEF);
	}

	double accelTrigger() {
		return accelTrigger.getDouble(1);
	}

	double chargeTime() {
		return chargeTimeEntry.getDouble(CHARGE_TIME_DEF);
	}

	double shootTime() {
		return shootTimeEntry.getDouble(SHOOT_TIME_DEF);
	}

	double lidTime() {
		return lidTimeEntry.getDouble(LID_TIME_DEF);
	}

}