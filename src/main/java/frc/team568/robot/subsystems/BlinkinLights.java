package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

public class BlinkinLights extends SubsystemBase {
	private Spark control;
	private Color _color;
	private SendableChooser<Color> colorChooser;

	public BlinkinLights(final RobotBase robot) {
		super(robot);
		control = new Spark(configInt("control"));
		SendableRegistry.addChild(this, control);
		setColor(Color.PINK_HOT);
		colorChooser = new SendableChooser<>();
		for (Color color : Color.values()) {
			if (color == getColor())
				colorChooser.setDefaultOption(color.name(), color);
			else
				colorChooser.addOption(color.name(), color);
		}
	}

	@Override
	public String getConfigName() {
		return "blinkin";
	}

	public Color getColor() {
		return _color;
	}

	public void setColor (Color color) {
		this._color = color;
		control.set(color.getCode());
	}

	public void setTeamColor() {
		Color teamColor;
		switch (DriverStation.getInstance().getAlliance()) {
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
		setColor(teamColor);
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.setSmartDashboardType("Blinkin");
		if (builder instanceof SendableBuilderImpl) {
			NetworkTable chooserTable = ((SendableBuilderImpl) builder).getTable().getSubTable("Light Color");
			SendableBuilderImpl ccBuilder = new SendableBuilderImpl();
			ccBuilder.setTable(chooserTable);
			colorChooser.initSendable(ccBuilder);
			ccBuilder.updateTable();
			ccBuilder.addStringProperty("selected", () -> getColor().name(), name -> setColor(Color.valueOf(name)));
			ccBuilder.startListeners();
			chooserTable.getEntry(".name").setString("Light Color");
		}
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
