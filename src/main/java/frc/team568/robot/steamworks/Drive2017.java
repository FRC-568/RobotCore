package frc.team568.robot.steamworks;

import frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive2017 extends CommandBase {
	DriveTrain drive;
	Timer timer;

	double speed;
	double delay;
	boolean forward;
	double inch;
	ReferenceFrame2017 ref;
	double sign;

	public Drive2017(double distance, double speed, DriveTrain drive, ReferenceFrame2017 ref) {
		this.drive = drive;
		this.ref = ref;
		inch = ref.motorEncoder.getDistance() + (distance);
		this.speed = speed;
		sign = distance;
	}

	@Override
	public void initialize() {
		timer = new Timer();
		timer.reset();
		timer.start();
		ref.motorEncoder.reset();
		ref.reset();
		// CM = SmartDashboard.getNumber("Centimeters", 0);
	}

	@Override
	public void execute() {
		drive.forwardWithGyro(speed);
		SmartDashboard.putNumber("GYRO", ref.getAngle());
	}

	@Override
	public boolean isFinished() {
		if (sign > 0)
			return ref.motorEncoder.getDistance() > inch;
		else
			return ref.motorEncoder.getDistance() < inch;
	}

	@Override
	public void end(boolean interrupted) {
		drive.halt();
		Timer.delay(.5);
	}

}
