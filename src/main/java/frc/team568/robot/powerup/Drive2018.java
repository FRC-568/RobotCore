package frc.team568.robot.powerup;

import static frc.team568.util.Utilities.*;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive2018 extends CommandBase {
	private static final double CIRCUMFERENCE = 18.8496;
	private static final double TPR = 4096; // Ticks per revolution
	private static final double TO_TICKS = TPR / CIRCUMFERENCE; // To Ticks from inches
	private static final double RAMP_UP = .25 * TPR;
	private static final double RAMP_DOWN = 1.2 * TPR;
	private static final double MIN_SPEED = 0.2;
	private static final double TARGET_DEADZONE = 2 * (TPR / CIRCUMFERENCE); // inches * convert_to_ticks

	DriveTrain2018 dt;
	double ticksToMove;
	double maxSpeed;
	double inch;
	double startingTicks;
	double targetTicks;
	double speedScale;
	boolean atTarget;
	double timeStamp;
	double error;

	public Drive2018(DriveTrain2018 dt, double inches, double maxSpeed) {
		this.dt = dt;
		addRequirements(dt);
		// dt.resetGyro();
		ticksToMove = inches * TO_TICKS;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public void initialize() {
		System.out.println("DRIVING " + ticksToMove + " TICKS");

		// dt.resetDist();
		dt.resetGyro();
		System.out.println(dt.getDist());
		startingTicks = dt.getDist();
		targetTicks = startingTicks + ticksToMove;

		dt.lockHeading();
	}

	@Override
	public void execute() {
		dt.driveDist(linearSpeedRamp(), targetTicks);
		System.out.println(linearSpeedRamp());
	}

	@Override
	public boolean isFinished() {
		return Math.abs(targetTicks - dt.getDist()) <= TARGET_DEADZONE;
	}

	@Override
	public void end(boolean interrupted) {
		System.out.println("Drive Finished");
		dt.releaseHeading();
		dt.stop();
	}

	private double linearSpeedRamp() {
		if (isFinished())
			return 0;

		double speedFactor = 1.0;
		if (dt.getDist() >= targetTicks - RAMP_DOWN) {
			speedFactor = (targetTicks - dt.getDist()) / RAMP_DOWN;
			System.out.println("Ramp Down");
		} else if (dt.getDist() < startingTicks + RAMP_UP) {
			speedFactor = (dt.getDist() - startingTicks) / RAMP_UP;
			System.out.println("Ramp Up");
		}

		if (speedFactor == 0)
			return MIN_SPEED;
		return Math.signum(maxSpeed * speedFactor) * clamp(Math.abs(maxSpeed * speedFactor), MIN_SPEED, 1);
	}

}
