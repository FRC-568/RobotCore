package frc.team568.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;

public final class ControlManager {
	private Map<String, BooleanSupplier> buttonMap = new HashMap<>();
	private Map<String, DoubleSupplier> axisMap = new HashMap<>();
	private Joystick[] jsCache = new Joystick[4];

	public void bindButton(int controller, int button, String key) {
		buttonMap.put(key, () -> getJoystick(controller).getRawButton(button));
	}

	public void bindAxis(int controller, int axis, String key) {
		axisMap.put(key, () -> getJoystick(controller).getRawAxis(axis));
	}

	public boolean button(String key) {
		return buttonMap.containsKey(key)
			? buttonMap.get(key).getAsBoolean()
			: false;
	}

	public double axis(String key) {
		return axisMap.containsKey(key)
			? axisMap.get(key).getAsDouble()
			: 0;
	}

	private Joystick getJoystick(int port) {
		if (port < 0 || port > 3)
			return null;

		Joystick js = jsCache[port];
		if (js == null) {
			js = new Joystick(port);
			jsCache[port] = js;
		}

		return js;
	}
}