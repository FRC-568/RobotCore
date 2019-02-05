package frc.team568.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class RobotBase extends TimedRobot implements PortMapper {
	private static final Preferences preferences = Preferences.getInstance();

	private final Map<Class<? extends Subsystem>, Subsystem> subsystems;
	private final String _name;

	protected RobotBase(final String name) {
		_name = name;
		subsystems = new HashMap<Class<? extends Subsystem>, Subsystem>();
	}
	
	//Returns a single IO port mapping - use in subsystems to lookup the location of a hardware device
	@Override
	public int getPort(String name) {
		String path = getName() + "/ports/" + name;
		if(!preferences.containsKey(path))
			throw new RuntimeException("A Subsystem has requested the port mapping '" + name + "', but it is not defined.");
		return preferences.getInt(path, -1);
	}

	public final String getName() {
		return _name;
	}
	
	//Returns an unmodifiable view of the entire port map
	public Map<String, Integer> getPortMap() {
		String prefix = getName() + "/ports/";

		return preferences.getKeys().stream()
		.filter(key -> key.startsWith(prefix))
		.collect(Collectors.toUnmodifiableMap(
			key -> key.substring(prefix.length()),
			value -> preferences.getInt(value, -1)));
	}
	
	//Adds a new port mapping - call in the constructor before initializing subsystems
	protected void port(String name, int port) {
		preferences.putInt(getName() + "/ports/" + name, port);
	}
	
	//Takes a constructor Function and returns the created subsystem. Subsystems are retrievable with getSubsystem()
	protected <S extends Subsystem> S addSubsystem(Function<RobotBase, S> constructor) {
		S subsystem = constructor.apply(this);
		subsystems.put(subsystem.getClass(), subsystem);
		return subsystem;
	}
	
	protected <S extends Subsystem> S addSubsystem(Class<? extends Subsystem> type, Function<RobotBase, S> constructor) {
		S subsystem = constructor.apply(this);
		return addSubsystem(type, subsystem);
	}
	
	protected <S extends Subsystem> S addSubsystem(Class<? extends Subsystem> type, S subsystem) {
		if(!type.isInstance(subsystem))
			throw new RuntimeException("Subsystem of type " + subsystem.getClass().getSimpleName()
					+ " is incompatible with " + type.getSimpleName());
		subsystems.put(type, subsystem);
		return subsystem;
	}
	
	@SuppressWarnings("unchecked")
	public <S extends Subsystem> S getSubsystem(Class<S> type) {
		return (S) subsystems.get(type);
	}

}
