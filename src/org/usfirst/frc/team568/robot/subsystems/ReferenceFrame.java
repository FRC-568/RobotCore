package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.ADIS16448_IMU;
import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ReferenceFrame extends Subsystem {
	ADIS16448_IMU imu;
	Timer time;
	double currentPosition = imu.getDisX();
	double startingPosition = currentPosition;
	double currentHeading = imu.getAngleZ();
	double wantedHeading = currentHeading;
	static double tolerance = 4;

	public ReferenceFrame() {
		imu = new ADIS16448_IMU();
		time = new Timer();

	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}

	public void startTimer() {
		time.start();
	}

	public void calabrateimu() {
		imu.calibrate();
	}

	public void resetData() {
		imu.reset();
	}

	public double getDistanceNeededToTravel(double inches) {
		double distanceNeededToTravel = Math.abs(currentPosition - startingPosition);
		return distanceNeededToTravel;

	}

	public void travelForwardToDistance(double inches, double speed) {
		while (getDistanceNeededToTravel(inches) > 0) {
			Robot.getInstance().drive.goForwards(speed);
		}
		Robot.getInstance().drive.halt();
	}

	public void travelBackwardsToDistance(double inches, double speed) {
		while (getDistanceNeededToTravel(inches) > 0) {
			Robot.getInstance().drive.goForwards(speed * -1);
		}
		Robot.getInstance().drive.halt();
	}

	public boolean stayTrueToHeading() {
		if (currentHeading - wantedHeading < tolerance) {
			Robot.getInstance().drive.applyPowerToLeftMotors(.5);
			Robot.getInstance().drive.applyPowerToRightMotors(-.5);
			return false;
		} else if (currentHeading - wantedHeading > tolerance) {
			Robot.getInstance().drive.applyPowerToLeftMotors(-.5);
			Robot.getInstance().drive.applyPowerToRightMotors(.5);
			return false;
		} else {
			return true;
		}

	}

	public boolean TurnToHeading(double speed, double position) {
		if (Math.abs(currentHeading - position) > tolerance) {
			Robot.getInstance().drive.applyPowerToLeftMotors(speed * -1);
			Robot.getInstance().drive.applyPowerToRightMotors(speed);
			return false;
		} else if (Math.abs(currentHeading) - position < tolerance) {
			Robot.getInstance().drive.applyPowerToLeftMotors(speed);
			Robot.getInstance().drive.applyPowerToRightMotors(-1 * speed);
			return false;
		} else {
			Robot.getInstance().drive.halt();
			return true;
		}
	}

	public double calculateTimeLeft() {
		double timeLeft = 15 - time.get();
		return timeLeft;
	}

}