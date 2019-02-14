package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.command.Command;

public class TapeTrackerCommand extends Command {
	DriveTrain2019 drive;
	GripPipeline pipeline;
	VisionTargetTracker tracker;

	private static final double MAX_SPEED = .4;
	private static final double MAX_DISTANCE = 80; // inches

	public TapeTrackerCommand(DriveTrain2019 drive) {
		this.drive = drive;
	}
	@Override
	public void initialize() {
		
		final double Kp = .02;

		double speed = MAX_SPEED * tracker.distanceFromTarget() / MAX_DISTANCE;
		if (speed > MAX_SPEED)
			speed = MAX_SPEED;

		if (tracker.getAngle() <= 2 && tracker.getAngle() >= -2) {
			drive.setSpeed(speed, speed);
		}

		else {
			double error = tracker.getAngle() * Kp;
			drive.setSpeed(speed - error, speed + error);
		}	}
	@Override
	public void execute() {

	}
	@Override
	public boolean isFinished() {
		return false;
	}

}