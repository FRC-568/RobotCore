package frc.team568.robot;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEvent.Kind;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;

public abstract class RobotBase extends TimedRobot implements PortMapper {
	private final String _name;
	private final NetworkTable config;
	private final ControlMapper controls;
	
	protected static final int UP = 0, RIGHT = 90, DOWN = 180, LEFT = 270;

	protected RobotBase(final String name) {
		_name = name;
		config = NetworkTableInstance.getDefault().getTable(getName());
		config.getEntry(".type").setString("RobotPreferences");
		config.addListener(EnumSet.of(Kind.kPublish),
			(table, key, event) -> event.topicInfo.getTopic().setPersistent(true));
		controls = new ControlMapper();
	}
	
	//Returns a single IO port mapping - use in subsystems to lookup the location of a hardware device
	@Override
	public int getPort(String name) {
		String path = getName() + "/ports/" + name;
		if(!Preferences.containsKey(path))
			throw new RuntimeException("A Subsystem has requested the port mapping '" + name + "', but it is not defined.");
		return Preferences.getInt(path, -1);
	}

	public final String getName() {
		return _name;
	}
	
	//Returns an unmodifiable view of the entire port map
	public Map<String, Integer> getPortMap() {
		String prefix = getName() + "/ports/";

		return Preferences.getKeys().stream()
		.filter(key -> key.startsWith(prefix))
		.collect(Collectors.toUnmodifiableMap(
			key -> key.substring(prefix.length()),
			value -> Preferences.getInt(value, -1)));
	}

	public NetworkTable getConfig() {
		return config;
	}

	public ControlMapper getControls() {
		return controls;
	}

	protected void button(String key, int controller, int button) {
		getControls().bindButton(key, controller, button);
	}

	protected void button(String key, BooleanSupplier supplier) {
		getControls().bindButton(key, supplier);
	}

	protected boolean button(int controller, int button) {
		return DriverStation.getStickButton(controller, button);
	}

	protected void pov(String key, int controller, int direction) {
		getControls().bindPOVButton(key, controller, direction);
	}

	protected void pov(String key, BooleanSupplier supplier) {
		getControls().bindButton(key, supplier);
	}

	protected boolean pov(int controller, int direction) {
		return DriverStation.getStickPOV(controller, 0) == direction;
	}

	protected void axis(String key, int controller, int axis) {
		getControls().bindAxis(key, controller, axis);
	}

	protected void axis(String key, DoubleSupplier supplier) {
		getControls().bindAxis(key, supplier);
	}

	protected double axis(int controller, int axis) {
		return DriverStation.getStickAxis(controller, axis);
	}
	
	//Adds a new port mapping - call in the constructor before initializing subsystems
	protected void port(String name, int port) {
		Preferences.setInt(getName() + "/ports/" + name, port);
	}
	
	// Set Subsystem configuration - call in the constructor before initializing subsystems
	protected void config(String key, boolean value) {
		getConfig().getEntry(key).setBoolean(value);
	}
	
	//Set Subsystem configuration - call in the constructor before initializing subsystems
	protected void config(String key, boolean[] value) {
		getConfig().getEntry(key).setBooleanArray(value);
	}
	
	// Set Subsystem configuration - call in the constructor before initializing subsystems
	protected void config(String key, Number value) {
		getConfig().getEntry(key).setNumber(value);
	}
	
	//Set Subsystem configuration - call in the constructor before initializing subsystems
	protected void config(String key, Number[] value) {
		getConfig().getEntry(key).setNumberArray(value);
	}

}
