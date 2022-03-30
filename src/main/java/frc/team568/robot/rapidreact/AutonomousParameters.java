package frc.team568.robot.rapidreact;

import static edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets.kNumberSlider;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

class AutonomousParameters {
	static final double TAXI_TIME_DEF = 1.5;
	static final double TAXI_DELAY_DEF = 10.0;
	static final double CHARGE_TIME_DEF = 1.0;
	static final double SHOOT_TIME_DEF = 3.0;
	static final double LID_TIME_DEF = 0.3;

	ShuffleboardTab tab;
	NetworkTableEntry taxiTimeEntry;
	NetworkTableEntry taxiDelayEntry;
	NetworkTableEntry chargeTimeEntry;
	NetworkTableEntry shootTimeEntry;
	NetworkTableEntry lidTimeEntry;

	AutonomousParameters() {
		setupShuffleboard();
	}

	void setupShuffleboard() {
		tab = Shuffleboard.getTab("Parameters");

		taxiTimeEntry = tab.add("Taxi Time", TAXI_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 4)).getEntry();
		taxiTimeEntry.setPersistent();

		taxiDelayEntry = tab.add("Taxi After", TAXI_DELAY_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0, "max", 15)).getEntry();
		taxiDelayEntry.setPersistent();

		chargeTimeEntry = tab.add("Lunge Timeout", CHARGE_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		chargeTimeEntry.setPersistent();

		shootTimeEntry = tab.add("Intake Timeout", SHOOT_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.05, "max", 1)).getEntry();
		shootTimeEntry.setPersistent();

		lidTimeEntry = tab.add("Lid Closing Delay", LID_TIME_DEF)
				.withWidget(kNumberSlider)
				.withProperties(Map.of("min", 0.1, "max", 5)).getEntry();
		lidTimeEntry.setPersistent();
	}

	double taxiTime() {
		return taxiTimeEntry.getDouble(TAXI_TIME_DEF);
	}

	double taxiDelay() {
		return taxiDelayEntry.getDouble(TAXI_DELAY_DEF);
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