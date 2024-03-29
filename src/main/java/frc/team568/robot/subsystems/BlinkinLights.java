package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BlinkinLights extends SubsystemBase {
	private final  Spark control;
	private Color _color;
	private SendableChooser<Color> colorChooser;

	public BlinkinLights(final int controlPort) {
		control = new Spark(controlPort);
		addChild("Controller", control);
		setColor(Color.PINK_HOT);
		colorChooser = new SendableChooser<>();
		for (Color color : Color.values()) {
			if (color == getColor())
				colorChooser.setDefaultOption(color.name(), color);
			else
				colorChooser.addOption(color.name(), color);
		}
	}

	public Color getColor() {
		return _color;
	}

	public void setColor (Color color) {
		this._color = color;
		control.set(color.getCode());
	}

	public void setTeamColor() {
		Color teamColor = Color.GREEN;
		if (DriverStation.getAlliance().isPresent()) {
			switch (DriverStation.getAlliance().get()) {
			case Blue:
				teamColor = Color.BLUE;
				break;
			case Red:
				teamColor = Color.RED;
				break;
			default:
				teamColor = Color.GREEN;
				break;
			}
		}
		
		setColor(teamColor);
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.setSmartDashboardType("Blinkin");
		builder.addStringProperty("Color", () -> getColor().name(), null);
	}

	public enum Color {
		RAINBOW(-0.99), RAINBOW_PARTY(-0.97), RAINBOW_OCEAN(-0.95), LARSON_SCANNER(-0.35), CUSTOM1_HEARTBEAT_SLOW(0.03),
		CUSTOM1_HEARTBEAT_MEDIUM(0.05), CUSTOM1_HEARTBEAT_FAST(0.07), PINK_HOT(0.57), RED_DARK(0.59), RED(0.61),
		RED_ORANGE(0.63), ORANGE(0.65), GOLD(0.67), YELLOW(0.69), GREEN_LAWN(0.71),
		LIME(0.73), GREEN_DARK(0.75), GREEN(0.77), BLUE_GREEN(0.79), AQUA(0.81),
		BLUE_SKY(0.83), BLUE_DARK(0.85), BLUE(0.87), BLUE_VIOLET(0.89), VIOLET(0.91),
		WHITE(0.93), GRAY(0.95), GRAY_DARK(0.97), BLACK(0.99);

		private final double _code;

		private Color(final double code) {
			this._code = code;
		}

		public double getCode() {
			return _code;
		}
	}
}
