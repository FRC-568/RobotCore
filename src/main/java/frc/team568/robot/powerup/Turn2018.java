package frc.team568.robot.powerup;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class Turn2018 extends PIDCommand {
	DriveTrain2018 dt;
	double degrees;
	double ra;
	double speedScale;
	boolean atTarget;
	double timeStamp;
	private static final double TimeToCheck = .5;
	// ReferenceFrame2017 ref;

	public Turn2018(DriveTrain2018 dt, double degrees) {
		super(0.04, 0.003, 0.0);
		this.dt = dt;
		requires(dt);
		this.degrees = degrees;
	}

	@Override
	protected void initialize() {
		System.out.println("TURNING " + degrees);

		ra = dt.getAngle() + degrees;
		setSetpoint(ra);
		System.out.println("Ref ANGLE: " + ra);
		// ref = Robot.getInstance().referenceFrame;
	}

	@Override
	protected void execute() {
		if (degrees > 0)
			dt.turnRight(.3 * speedScale);
		else if (degrees < 0)
			dt.turnRight(.3 * speedScale);
		else
			dt.stop();
	}

	@Override
	protected boolean isFinished() {

		if (Math.abs(getPIDController().getSetpoint() - dt.getAngle()) <= 5) {
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
		System.out.println("turn Finished");
		dt.stop();
		// Timer.delay(1);
	}

	@Override
	protected double returnPIDInput() {
		return dt.getAngle();

	}

	@Override
	protected void usePIDOutput(double output) {
		speedScale = output;

	}

}
