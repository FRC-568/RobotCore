package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.powerup.DriveTrain2018;

import edu.wpi.first.wpilibj.command.Command;

public class Drive2018 extends Command {
	DriveTrain2018 dt;
	double dist;
	double speed;
	double inch;
	double distToTravel;

	private static final double CIRCUMFERENCE = 18.8496;
	// GEAR RATIO
	private static final double GEARING_FACTOR = 1 / 10.71;
	// Ticks per revolution
	private static final double TPR = 4096;
	// To Ticks from inches
	private static final double TO_TICKS = TPR / CIRCUMFERENCE;

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

		dt.resetDist();
		dt.resetGyro();
		distToTravel = dt.getDist() + dist;

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
		dt.stop();
	}

}
