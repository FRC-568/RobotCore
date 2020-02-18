package frc.team568.robot.powerup;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Turn2018 extends CommandBase {
	final DriveTrain2018 dt;
	final PIDController controller;
	final double degrees;
	double ra;
	boolean atTarget;
	double timeStamp;
	private static final double TimeToCheck = .5;

	public Turn2018(final DriveTrain2018 dt, final double degrees) {
		this.dt = dt;
		this.controller = new PIDController(0.04, 0.003, 0.0);
		addRequirements(dt);
		this.degrees = degrees;
	}

	@Override
	public void initialize() {
		ra = dt.getAngle() + degrees;
		controller.setSetpoint(ra);
	}

	@Override
	public void execute() {
		double speedScale = controller.calculate(dt.getAngle());
		if (degrees > 0)
			dt.turnRight(.3 * speedScale);
		else if (degrees < 0)
			dt.turnRight(.3 * speedScale);
		else
			dt.stop();
	}

	@Override
	public boolean isFinished() {
		if (Math.abs(controller.getSetpoint() - dt.getAngle()) <= 5) {
			if (atTarget) {
				if ((Timer.getFPGATimestamp() - timeStamp) >= TimeToCheck) {
					atTarget = false;
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
	public void end(boolean interrupted) {
		dt.stop();
	}

}
