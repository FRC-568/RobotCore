package frc.team568.robot.powerup;

import frc.team568.robot.powerup.DriveTrain2018;
import frc.team568.robot.powerup.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Turn extends CommandBase {
	DriveTrain2018 drive;
	double degrees;
	double ra;

	// ReferenceFrame2017 ref;

	public Turn(double degrees) {
		this.degrees = degrees;
		ra = drive.getAngle() + degrees;
		System.out.println("Ref ANGLE: " + ra);
	}

	public Turn() {}

	@Override
	public void initialize() {
		drive = Robot.getInstance().driveTrain;
		ra = drive.getAngle() + degrees;
		System.out.println("Ref ANGLE: " + ra);
		// ref = Robot.getInstance().referenceFrame;
	}

	@Override
	public void execute() {
		// SmartDashboard.putNumber("GYRO", drive.getAngle());
		if (degrees > 0)
			drive.turnRight(.3);
		else if (degrees < 0)
			drive.turnLeft(.3);
		else
			drive.stop();
	}

	@Override
	public boolean isFinished() {
		if (degrees < 0) {
			return drive.getAngle() < (ra);
		} else if (degrees > 0) {
			return drive.getAngle() > (ra);
		} else
			return true;

	}

	@Override
	public void end(boolean interrupted) {
		drive.stop();
		Timer.delay(1);
	}

}
