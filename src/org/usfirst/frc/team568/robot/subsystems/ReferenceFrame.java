package org.usfirst.frc.team568.robot.subsystems;

import org.usfirst.frc.team568.robot.ADIS16448_IMU;
import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ReferenceFrame extends Subsystem {
	public ADIS16448_IMU imu;
	Timer time;
	// double currentPosition = imu.getDisX();
	double startingPosition = 0;
	// double currentHeading = imu.getAngleZ();
	/// double wantedHeading = currentHeading;
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

	public void travelForwardToDistance(double speed) {

		Robot.getInstance().meccanumDrive.goForwards(speed);
	}

	public void travelBackwardsToDistance(double inches, double speed) {

		Robot.getInstance().meccanumDrive.goForwards(speed * -1);

	}

	public boolean stayTrueToHeading(double wantedHeading) {
		if (imu.getAngleZ() - wantedHeading < tolerance) {
			Robot.getInstance().meccanumDrive.applyPowerToLeftMotors(.5);
			Robot.getInstance().meccanumDrive.applyPowerToRightMotors(-.5);
			return false;
		} else if (imu.getAngleZ() - wantedHeading > tolerance) {
			Robot.getInstance().meccanumDrive.applyPowerToLeftMotors(-.5);
			Robot.getInstance().meccanumDrive.applyPowerToRightMotors(.5);
			return false;
		} else {
			return true;
		}
	}

	public boolean TurnToHeading(double speed, double position) {
		if (Math.abs(imu.getAngleZ() - position) > tolerance) {
			Robot.getInstance().meccanumDrive.applyPowerToLeftMotors(speed * -1);
			Robot.getInstance().meccanumDrive.applyPowerToRightMotors(speed);
			return false;
		} else if (Math.abs(imu.getAngleZ()) - position < tolerance) {
			Robot.getInstance().meccanumDrive.applyPowerToLeftMotors(speed);
			Robot.getInstance().meccanumDrive.applyPowerToRightMotors(-1 * speed);
			return false;
		} else {
			Robot.getInstance().meccanumDrive.halt();
			return true;
		}
	}

	public double calculateTimeLeft() {
		double timeLeft = 15 - time.get();
		return timeLeft;
	}

}