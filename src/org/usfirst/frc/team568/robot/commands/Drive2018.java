package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.DriveTrain2018;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class Drive2018 extends PIDCommand {
	DriveTrain2018 dt;
	double dist;
	double speed;
	double inch;
	double distToTravel;
	double speedScale;
	boolean atTarget;
	double timeStamp;
	private static final double TimeToCheck = .5;

	private static final double CIRCUMFERENCE = 18.8496;
	private static final double TPR = 4096; // Ticks per revolution
	private static final double TO_TICKS = TPR / CIRCUMFERENCE; // To Ticks from inches

	public Drive2018(DriveTrain2018 dt, double inch, double speed) {
		super(.01, 0, 0);
		this.dt = dt;
		requires(dt);
		// dt.resetGyro();
		dist = inch * TO_TICKS;
		this.speed = speed;

	}

	@Override
	protected void initialize() {
		System.out.println("DRIVING " + dist + " TICKS");

		dt.resetDist();
		dt.resetGyro();
		distToTravel = dt.getDist() + dist;
		setSetpoint(distToTravel);

		dt.drivePID.setSetpoint(dt.getAngle());
		dt.drivePID.enable();

	}

	@Override
	protected void execute() {
		dt.driveDist(speedScale * speed, distToTravel);
	}

	@Override
	protected boolean isFinished() {

		if (Math.abs(dt.getDist() - distToTravel) <= .5) {
			if (atTarget) {
				if ((Timer.getFPGATimestamp() - timeStamp) >= TimeToCheck) {
					return true;
				}
			} else {
				atTarget = true;
				timeStamp = Timer.getFPGATimestamp();
			}
		} else {
			atTarget = false;
		}
		return false;
	}

	@Override
	protected void end() {
		dt.drivePID.disable();
		dt.stop();
	}

	@Override
	protected double returnPIDInput() {

		return dt.getDist();
	}

	@Override
	protected void usePIDOutput(double output) {
		speedScale = output;

	}

}
