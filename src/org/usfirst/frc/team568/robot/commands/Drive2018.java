package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.DriveTrain2018;

import edu.wpi.first.wpilibj.command.Command;

public class Drive2018 extends Command {
	DriveTrain2018 dt;
	double dist;
	double speed;
	double inch;
	double distToTravel;
	double speedScale;
	boolean atTarget;
	double timeStamp;
	double error;
	private static final double TimeToCheck = .5;

	private static final double CIRCUMFERENCE = 18.8496;
	private static final double TPR = 4096; // Ticks per revolution
	private static final double TO_TICKS = TPR / CIRCUMFERENCE; // To Ticks from inches

	private static final double StartDist = 1 * TPR;
	private static final double EndDist = 1 * TPR;

	public Drive2018(DriveTrain2018 dt, double inch, double speed) {

		this.dt = dt;
		requires(dt);
		// dt.resetGyro();
		dist = inch * TO_TICKS;
		this.speed = speed;

	}

	@Override
	protected void initialize() {
		System.out.println("DRIVING " + dist + " TICKS");

		// dt.resetDist();
		dt.resetGyro();
		System.out.println(dt.getDist());
		distToTravel = dt.getDist() + dist;

		dt.drivePID.setSetpoint(dt.getAngle());
		dt.drivePID.enable();

	}

	@Override
	protected void execute() {

		dt.driveDist(speed, distToTravel);
	}

	@Override
	protected boolean isFinished() {
		return dt.getDist() >= distToTravel;

	}

	@Override
	protected void end() {
		System.out.println("Drive Finished");
		dt.drivePID.disable();
		dt.stop();
	}

}
