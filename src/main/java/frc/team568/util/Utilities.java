package frc.team568.util;

public final class Utilities {
	private Utilities() {
	}

	public static double clamp(double value, double min, double max) {
		return (value > max) ? max : ((value < min) ? min : value);
	}

	public static double applyDeadband(double value, double deadband) {
		if (Math.abs(value) > deadband) {
			if (value > 0.0)
				return (value - deadband) / (1.0 - deadband);
			else
				return (value + deadband) / (1.0 - deadband);
		} else
			return 0.0;
	}

	public static final double lerp(double startValue, double endValue, double percent) {
		return startValue + ((endValue - startValue) * clamp(percent, 0, 1));
	}
}
