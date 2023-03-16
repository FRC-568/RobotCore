package frc.team568.robot.chargedup;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDistanceIGuess extends CommandBase {
	SwerveSubsystem drive;
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
		if (drive.getPose().getX() < xDist) {
			if (drive.getPose().getY() < yDist) {
				drive.drive(xSpeed, ySpeed, 0);
			} else {
				drive.drive(xSpeed, 0, 0);
			}
		} else {
			if (drive.getPose().getY() < yDist) {
				drive.drive(0, ySpeed, 0);
			}
		}
	}

	@Override
	public boolean isFinished() {
		return drive.getPose().getX() >= xDist && drive.getPose().getY() >= yDist;
	}

	@Override
	public void end(boolean interrupted) {
		drive.drive(0, 0, 0);
	}
}
