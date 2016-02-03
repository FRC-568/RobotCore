package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.ADIS16448_IMU;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ReferenceFrame extends Subsystem {
	ADIS16448_IMU imu;
	Timer time;

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

	public double calculateDistanceLeft(double distanceWantedToTravel) {
		double distanceLeft = imu.getDisX() - distanceWantedToTravel;
		return distanceLeft;
	}

	public double calculateHeading() {
		return imu.getAngleZ();
	}

	public double calculateTimeLeft() {
		double timeLeft = 15 - time.get();
		return timeLeft;
	}

}