package frc.team568.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class DriveForTime extends Command {
	
	private final Timer timer = new Timer();
	private final double speed;
	private final double time;
	private final TalonSRXDrive driveTrain;

	public DriveForTime(double time, double speed, TalonSRXDrive driveTrain) {
		
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

		driveTrain.setSpeed(speed);
	
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
