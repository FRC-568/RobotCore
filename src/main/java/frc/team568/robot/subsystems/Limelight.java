package frc.team568.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.team568.robot.RobotBase;

public class Limelight extends SubsystemBase {

	private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

	// Variables for calculations
	private double cameraHeight = 0;
	private double cameraAngle = 0;
	private double targetHeight = 0;

	public Limelight(RobotBase robot) {

		super(robot);

	}

	public boolean isTargetValid() {

		double tv = table.getEntry("tv").getDouble(0);

		if (tv == 0) return false;
		else return true;

	}

	public double getOffsetX() {

		return table.getEntry("tx").getDouble(0);

	}

	public double getOffsetY() {

		return table.getEntry("ty").getDouble(0);

	}

	public double getTargetArea() {

		return table.getEntry("ta").getDouble(0);

	}

	public double getSkew() {

		return table.getEntry("ts").getDouble(0);

	}

	public int getPipe() {

		return (int) table.getEntry("getpipe").getDouble(0);

	}

	public void setPipeline(int index) {

		table.getEntry("pipeline").setNumber(index);

	}

	public void setCameraHeight(double cameraHeight) {

		this.cameraHeight = cameraHeight;

	}

	public void setCameraAngle(double cameraAngle) {

		this.cameraAngle = cameraAngle;

	}

	public void setTargetHeight(double targetHeight) {

		this.targetHeight = targetHeight;

	}

	public double getDistance() {

		return (targetHeight - cameraHeight) / Math.tan(Math.toRadians(cameraAngle) + Math.toRadians(getOffsetY()));

	}

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);

		builder.addBooleanProperty("Is Target Valid", () -> isTargetValid(), null);
		builder.addDoubleProperty("Offset X", () -> getOffsetX(), null);
		builder.addDoubleProperty("Offset Y", () -> getOffsetY(), null);
		builder.addDoubleProperty("Area", () -> getTargetArea(), null);
		builder.addDoubleProperty("Skew", () -> getSkew(), null);
		builder.addDoubleProperty("Pipeline", () -> getPipe(), null);
		builder.addDoubleProperty("Distance", () -> getDistance(), null);

	}

}