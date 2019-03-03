package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Spark;

public class BlinkinLights extends SubsystemBase {
	private Spark control;

	public BlinkinLights(final RobotBase robot) {
		super(robot);
		control = new Spark(port("BlinkinLights"));
		addChild(control);
	}

	public void setColor (Color color) {
		control.set(color.getCode());
	}
	public enum Color {
		RAINBOW(-0.99), RAINBOW_PARTY(-0.97), RAINBOW_OCEAN(-0.95), LARSON_SCANNER(-0.35), CUSTOM1_HEARTBEAT_SLOW(
				0.03), CUSTOM1_HEARTBEAT_MEDIUM(0.05), CUSTOM1_HEARTBEAT_FAST(0.07),

		PINK_HOT(0.57), RED_DARK(0.59), RED(0.61), RED_ORANGE(0.63), ORANGE(0.65), GOLD(0.67), YELLOW(0.69), GREEN_LAWN(
				0.71), LIME(0.73), GREEN_DARK(0.75), GREEN(0.77), BLUE_GREEN(0.79), AQUA(0.81), BLUE_SKY(
						0.83), BLUE_DARK(0.85), BLUE(0.87), BLUE_VIOLET(
								0.89), VIOLET(0.91), WHITE(0.93), GRAY(0.95), GRAY_DARK(0.97), BLACK(0.99);

		private final double code;

		private Color(double code) {
			this.code = code;
		}

		public double getCode() {
			return code;
		}
	}
}
