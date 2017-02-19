package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.VisionTargetTracker;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class MoveToVisionTarget extends Command {
	private final DriveTrain driveTrain;
	private final VisionTargetTracker vision;
	private static final double MAX_SPEED = .4;
	private static final double MAX_DISTANCE = 72; // inches

	public MoveToVisionTarget(final DriveTrain driveTrain, final VisionTargetTracker vision) {
		this.driveTrain = driveTrain;
		this.vision = vision;

	}

	protected void initialize() {

	}

	protected void execute() {
		SpeedController leftFront = driveTrain.leftFront;
		SpeedController leftBack = driveTrain.leftBack;
		SpeedController rightFront = driveTrain.rightFront;
		SpeedController rightBack = driveTrain.rightBack;

		double speed = MAX_SPEED * vision.distanceFromTarget() / MAX_DISTANCE;

		if (speed > MAX_SPEED)
			speed = MAX_SPEED;

		leftFront.setInverted(false);
		leftBack.setInverted(false);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		double Kp = .156;
		// Kp = SmartDashboard.getNumber("Kp", .15);
		double error = vision.getAngle() * Kp;

		// myDrive.drive(.5, error);

		if (vision.getAngle() <= 1 && vision.getAngle() >= -1) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(speed);
			rightBack.set(speed);
		} else {
			System.out.println(error);
			leftFront.set(speed - error);
			leftBack.set(speed - error);
			rightFront.set(speed + error);
			rightBack.set(speed + error);
		}

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
