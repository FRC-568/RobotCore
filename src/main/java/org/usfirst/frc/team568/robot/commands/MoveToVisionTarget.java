package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.VisionTargetTracker;

import edu.wpi.first.wpilibj.command.Command;

public class MoveToVisionTarget extends Command {
	private final DriveTrain driveTrain;
	private final VisionTargetTracker vision;
	private static final double MAX_SPEED = .4;
	private static final double MAX_DISTANCE = 112; // inches

	public MoveToVisionTarget(final DriveTrain driveTrain, final VisionTargetTracker vision) {
		this.driveTrain = driveTrain;
		this.vision = vision;
	}

	@Override
	protected void execute() {
		final double Kp = .02;

		double speed = MAX_SPEED * vision.distanceFromTarget() / MAX_DISTANCE;
		if (speed > MAX_SPEED)
			speed = MAX_SPEED;

		if (vision.getAngle() <= 2 && vision.getAngle() >= -2) {
			driveTrain.setSpeed(speed, speed);
		}

		else {
			double error = vision.getAngle() * Kp;
			driveTrain.setSpeed(speed - error, speed + error);
		}

	}

	@Override
	protected boolean isFinished() {
		if (vision.getAngle() <= 2 && vision.getAngle() >= -2) {
			return true;
		} else {
			return false;
		}

	}

}
