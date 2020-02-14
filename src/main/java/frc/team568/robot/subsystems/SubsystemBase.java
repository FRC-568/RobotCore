package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;

import java.util.Arrays;

import edu.wpi.first.networktables.NetworkTable;

import static edu.wpi.first.wpilibj.DriverStation.reportWarning;

public abstract class SubsystemBase extends edu.wpi.first.wpilibj2.command.SubsystemBase {
	public final RobotBase robot;
	public final NetworkTable config;

	public SubsystemBase(final RobotBase robot) {
		this.robot = robot;
		config = robot.getConfig().getSubTable(getConfigName());
	}

	public SubsystemBase(String name, final RobotBase robot) {
		this(robot);
		setName(name);
	}

	public Class<? extends SubsystemBase> getEffectiveClass() {
		return getClass();
	}

	public String getConfigName() {
		return getName();
	}
	
	protected int port(String name) {
		return robot.getPort(name);
	}

	protected boolean configBoolean(String key) {
		if (!config.containsKey(key)) {
			reportWarning("Configuration is missing: " + config.getPath() + "/" + key, false);
			config.getEntry(key).setBoolean(false);
		}
		return config.getEntry(key).getBoolean(false);
	}

	protected int configInt(String key) {
		if (!config.containsKey(key)) {
			reportWarning("Configuration is missing: " + config.getPath() + "/" + key, false);
			config.getEntry(key).setNumber(-1);
		}
		return config.getEntry(key).getNumber(-1).intValue();
	}

	protected int[] configIntArray(String key) {
		if (!config.containsKey(key)) {
			reportWarning("Configuration is missing: " + config.getPath() + "/" + key, false);
			config.getEntry(key).setNumberArray(new Number[0]);
			return new int[0];
		}
		return Arrays.stream(config.getEntry(key).getNumberArray(new Number[0]))
			.mapToInt(n -> n.intValue())
			.toArray();
	}

	protected boolean button(String key) {
		return robot.getControls().button(key);
	}

	protected double axis(String key) {
		return robot.getControls().axis(key);
	}

}
