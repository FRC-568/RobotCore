package frc.team568.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;

public class Limelight extends SubsystemBase {

	private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

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

	public double getDistance(double cameraHeight, double cameraAngle, double targetHeight) {

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
		
	}

}