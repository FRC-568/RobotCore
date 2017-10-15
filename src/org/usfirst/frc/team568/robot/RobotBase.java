package org.usfirst.frc.team568.robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.IterativeRobot;

public abstract class RobotBase extends IterativeRobot implements PortMapper {
	private final Map<String, Integer> _portMap;

	public RobotBase() {
		_portMap = new HashMap<String, Integer>();
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

}
