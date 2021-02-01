package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class AutoShooter extends InstantCommand {
	Shooter2016 shooter;

	public AutoShooter(Shooter2016 shooter) {
		this.shooter = shooter;
	}

	@Override
	public void execute() {
		if (SmartDashboard.getBoolean("Forward?", true)) {
			shooter.tiltUp();
			Timer.delay(.75);
			shooter.stopTilt();
		}
	}

}
