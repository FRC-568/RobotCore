package frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class BallShooterandMixer extends CommandBase {
	Shooter2017 shooter;

	public BallShooterandMixer(Shooter2017 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void initialize() {
		shooter.shootMotor.set(-(9.5 / 12));
		Timer.delay(.5);
	}

	@Override
	public void execute() {
		// Timer.delay(1);
		shooter.ballWranglerIn.set(true);
		shooter.ballWranglerOut.set(false);
		Timer.delay(250);
		shooter.gate.setAngle(50);
		Timer.delay(250);
		shooter.ballWranglerIn.set(false);
		shooter.ballWranglerOut.set(true);
		Timer.delay(250);
		shooter.gate.setAngle(0);
		Timer.delay(250);
	}

	@Override
	public void end(boolean interrupted) {
		shooter.shootMotor.set(0);
	}

}
