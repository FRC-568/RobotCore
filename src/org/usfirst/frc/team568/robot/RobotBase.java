package org.usfirst.frc.team568.robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class RobotBase extends IterativeRobot implements PortMapper {
	private final Map<String, Integer> _portMap;
	private final Map<Class<? extends Subsystem>, Subsystem> subsystems;

	public RobotBase() {
		_portMap = new HashMap<String, Integer>();
		subsystems = new HashMap<Class<? extends Subsystem>, Subsystem>();
	}
	
	//Returns a single IO port mapping - use in subsystems to lookup the location of a hardware device
	@Override
	public int getPort(String name) {
		if(!_portMap.containsKey(name))
			throw new RuntimeException("A Subsystem has requested the port mapping '" + name + "', but it is not defined.");
		return _portMap.get(name);
	}
	
	//Returns an unmodifiable view of the entire port map
	public Map<String, Integer> getPortMap() {
		return Collections.unmodifiableMap(_portMap);
	}
	
	//Adds a new port mapping - call in the constructor before initializing subsystems
	protected void port(String name, int port) {
		_portMap.put(name,  port);
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
