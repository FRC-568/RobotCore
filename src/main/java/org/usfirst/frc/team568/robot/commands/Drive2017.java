package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.steamworks.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2017;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive2017 extends Command {
	DriveTrain drive;
	Timer timer;

	double speed;
	double delay;
	boolean forward;
	double inch;
	ReferenceFrame2017 ref;
	double sign;

	public Drive2017(double distance, double speed) {
		ref = Robot.getInstance().referenceFrame;
		inch = ref.motorEncoder.getDistance() + (distance);
		this.speed = speed;
		sign = distance;

	}

	public Drive2017() {
		ref = Robot.getInstance().referenceFrame;
		SmartDashboard.getNumber("Speed", 0);
		inch = 0;
	}

	@Override
	protected void initialize() {
		drive = Robot.getInstance().driveTrain;
		timer = new Timer();
		timer.reset();
		timer.start();
		ref.motorEncoder.reset();
		ref.reset();
		// CM = SmartDashboard.getNumber("Centimeters", 0);
	}

	@Override
	protected void execute() {
		drive.forwardWithGyro(speed);
		SmartDashboard.putNumber("GYRO", ref.getAngle());
	}

	@Override
	protected boolean isFinished() {
		if (sign > 0)
			return ref.motorEncoder.getDistance() > inch;
		else
			return ref.motorEncoder.getDistance() < inch;
	}

	@Override
	protected void end() {
		drive.halt();
		Timer.delay(.5);
	}

}
