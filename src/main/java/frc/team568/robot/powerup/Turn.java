package frc.team568.robot.powerup;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Turn extends CommandBase {
	DriveTrain2018 drive;
	double degrees;
	double ra;

	// ReferenceFrame2017 ref;

	public Turn(DriveTrain2018 drive, double degrees) {
		this.drive = drive;
		this.degrees = degrees;
	}

	public Turn(DriveTrain2018 drive) {
		this.drive = drive;
	}

	@Override
	public void initialize() {
		ra = drive.getAngle() + degrees;
		System.out.println("Ref ANGLE: " + ra);
	}

	@Override
	public void execute() {
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
