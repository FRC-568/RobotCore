package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class AutoShooter extends InstantCommand {
	Shooter2016 shooter;

	@Override
	public void initialize() {
		shooter = Robot.getInstance().shooter;
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
