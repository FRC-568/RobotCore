package frc.team568.robot.bart;

import frc.team568.robot.subsystems.WestCoastDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveForward extends CommandBase {
	private final Timer timer = new Timer();
	private final double time;
	public final WestCoastDrive drive;
 
	public DriveForward(final WestCoastDrive drive, final double time) {
		this.drive = drive;
		this.time = time;
	}
	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	@Override
	public void execute() {
		drive.driveForward();
	}

	@Override
	public boolean isFinished() {
		return timer.hasElapsed(time);
	}

	@Override
	public void end(boolean interrupted) {
		timer.stop();
		drive.stop();
	}
}
