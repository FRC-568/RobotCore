package frc.team568.robot.commands;

import frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveForTime extends CommandBase {
	private final Timer timer = new Timer();
	private final double speed;
	private final double time;
	private final DriveTrain driveTrain;

	public DriveForTime(double time, double speed, DriveTrain driveTrain) {
		this.time = time;
		this.speed = speed;
		this.driveTrain = driveTrain;
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	public void execute() {
		driveTrain.setSpeed(speed, speed);
	}

	@Override
	public boolean isFinished() {
		return timer.hasElapsed(time);
	}

	@Override
	public void end(boolean interrupted) {
		timer.stop();
		driveTrain.halt();
	}

}
