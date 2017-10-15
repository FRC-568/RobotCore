package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.PortMapper;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SubsystemBase extends Subsystem {
	private final PortMapper _portMapper;

	public SubsystemBase(PortMapper source) {
		_portMapper = source;
	}

	public SubsystemBase(String name, PortMapper source) {
		super(name);
		_portMapper = source;
	}
	
	protected int port(String name) {
		return _portMapper.getPort(name);
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}

}
