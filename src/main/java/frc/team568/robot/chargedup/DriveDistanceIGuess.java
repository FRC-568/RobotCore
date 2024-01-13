package frc.team568.robot.chargedup;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

public class DriveDistanceIGuess extends Command {
	SwerveSubsystem drive;
	Pose2d initPose;
	double xDist = 0.0;
	double yDist = 0.0;
	double xSpeed = 0.0;
	double ySpeed = 0.0;

	DriveDistanceIGuess(SwerveSubsystem drive, double xDist, double yDist, double xSpeed, double ySpeed) {
		addRequirements(drive);
		this.drive = drive;
		this.xDist = xDist;
		this.yDist = yDist;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}

	@Override
	public void initialize() {
		drive.drive(0, 0, 0);
	}

	@Override
	public void execute() {
		// TODO: make this not extremely bad
		// if (drive.getPose().getX() < xDist) {
		// 	if (drive.getPose().getY() < yDist) {
		// 		drive.drive(xSpeed, ySpeed, 0);
		// 	} else {
		// 		drive.drive(xSpeed, 0, 0);
		// 	}
		// } else {
		// 	if (drive.getPose().getY() < yDist) {
		// 		drive.drive(0, ySpeed, 0);
		// 	}
		// }
		drive.drive(xSpeed, 0, 0);
		initPose = drive.getPose();
	}

	@Override
	public boolean isFinished() {
		// return drive.getPose().getX() >= xDist && drive.getPose().getY() >= yDist;
		return (drive.getPose().getX() - initPose.getX()) >= xDist;
	}

	@Override
	public void end(boolean interrupted) {
		drive.drive(0, 0, 0);
	}
}
