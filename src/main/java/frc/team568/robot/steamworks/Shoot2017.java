package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Shoot2017 extends CommandBase {
	public Shooter2017 shooter;
	private boolean gateState;
	private double timeStamp;
	private boolean rampedUp;

	public Shoot2017() {
		shooter = Robot.getInstance().shooter;
	}

	@Override
	public void initialize() {
		shooter.shootMotor.set(-(1.0));
		gateState = false;
		timeStamp = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
		shooter.shootMotor.set(-(1));
		if (!rampedUp) {
			if ((Timer.getFPGATimestamp() - timeStamp) >= 1.75)
				rampedUp = true;
		} else if ((Timer.getFPGATimestamp() - timeStamp) >= .75) {
			if (gateState) {
				shooter.gate.setAngle(0);
				gateState = false;
			} else {
				shooter.gate.setAngle(100);
				gateState = true;
			}
			timeStamp = Timer.getFPGATimestamp();
		}
	}

	@Override
	public void end(boolean interrupted) {
		shooter.shootMotor.set(0);
		shooter.gate.setAngle(0);
		gateState = false;
		rampedUp = false;
	}
}
