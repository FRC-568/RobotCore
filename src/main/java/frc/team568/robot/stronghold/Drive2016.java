package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive2016 extends CommandBase {
	ArcadeDrive drive;
	Timer timer;
	double speed;
	double delay;
	boolean forward;

	@Override
	public void initialize() {
		drive = Robot.getInstance().arcadeDrive;
		timer = new Timer();
		speed = SmartDashboard.getNumber("Speed", 0.0);
		delay = SmartDashboard.getNumber("Time?", 0.0);
		forward = SmartDashboard.getBoolean("Forward?", true);
		timer.reset();
		timer.start();
	}

	@Override
	public void execute() {
		if (forward) {
			drive.forwardWithGyro(speed);
		} else if (!forward) {
			drive.reverseWithGyro(speed);
		}
	}

	@Override
	public boolean isFinished() {
		return timer.get() >= delay;
	}

	@Override
	public void end(boolean interrupted) {
		drive.halt();
	}

}
