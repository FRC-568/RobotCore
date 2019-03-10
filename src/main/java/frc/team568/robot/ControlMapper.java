package frc.team568.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;

public final class ControlMapper {
	private static final DriverStation ds = DriverStation.getInstance();
	
	private Map<String, BooleanSupplier> buttonMap = new HashMap<>();
	private Map<String, DoubleSupplier> axisMap = new HashMap<>();

	public static final int UP = 0, RIGHT = 90, DOWN = 180, LEFT = 270;

	public void bindButton(String key, BooleanSupplier supplier) {
		buttonMap.put(key, supplier);
	}

	public void bindButton(String key, int controller, int button) {
		bindButton(key, () -> ds.getStickButton(controller, button));
	}

	public void bindPOVButton(String key, int controller, int direction) {
		bindButton(key, () -> ds.getStickPOV(controller, 0) == direction);
	}

	public void bindAxis(String key, DoubleSupplier supplier) {
		axisMap.put(key, supplier);
	}

	public void bindAxis(String key, int controller, int axis) {
		bindAxis(key, () -> ds.getStickAxis(controller, axis));
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
}