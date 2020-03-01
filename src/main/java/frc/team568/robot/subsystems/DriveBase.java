package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;

public abstract class DriveBase extends SubsystemBase {
	private double _wheelDiameter = 6.0;

	public DriveBase(final RobotBase robot) {
		super(robot);
	}

	public DriveBase(String name, final RobotBase robot) {
		super(name, robot);
	}

	public enum Input { FORWARD, STRAFE, TURN, TANK_LEFT, TANK_RIGHT }

	@Override
	public Class<? extends SubsystemBase> getEffectiveClass() {
		return DriveBase.class;
	}

	@Override
	public String getConfigName() {
		return "drive";
	}

	public void tankDrive(double leftValue, double rightValue) {
		tankDrive(leftValue, rightValue, true);
	}

	public abstract void tankDrive(double leftValue, double rightValue, boolean squareInputs);

	public void arcadeDrive(double moveValue, double rotateValue) {
		arcadeDrive(moveValue, rotateValue, true);
	}

	public abstract void arcadeDrive(double moveValue, double rotateValue, boolean squareInputs);

	public double getWheelDiameter() {
		return _wheelDiameter;
	}

	public void setWheelDiameter(double diameter) {
		this._wheelDiameter = diameter;
	}

	public double getWheelCircumference() {
		return getWheelDiameter() * Math.PI;
	}

	public abstract double getHeading();

	public abstract double getTurnRate();

	public abstract double getVelocityInTicks();

	public abstract double getVelocityInTicks(Side side);

	public abstract double getDistanceInTicks();

	public abstract double getDistanceInTicks(Side side);

	public abstract void resetSensors();

	public enum Side {LEFT, RIGHT}
}