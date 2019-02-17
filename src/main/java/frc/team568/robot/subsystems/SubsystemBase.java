package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;

import java.util.Arrays;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SubsystemBase extends Subsystem {
	public final RobotBase robot;
	public final NetworkTable config;

	public SubsystemBase(final RobotBase robot) {
		this.robot = robot;
		config = robot.getConfig().getSubTable(getConfigName());
	}

	public SubsystemBase(String name, final RobotBase robot) {
		super(name);
		this.robot = robot;
		config = robot.getConfig().getSubTable(getConfigName());
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
		return config.getEntry(key).getBoolean(false);
	}

	protected int configInt(String key) {
		return config.getEntry(key).getNumber(-1).intValue();
	}

	protected int[] configIntArray(String key) {
		if (!config.containsKey(key))
			return new int[0];

		return Arrays.stream(config.getEntry(key).getNumberArray(new Number[0]))
			.mapToInt(n -> n.intValue())
			.toArray();
	}
	
	@Override
	protected void initDefaultCommand() {
	}

}
