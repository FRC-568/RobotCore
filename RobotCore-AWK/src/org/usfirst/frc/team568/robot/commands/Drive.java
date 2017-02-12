package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Command {
	DriveTrain drive;
	Timer timer;

	double speed;
	double delay;
	boolean forward;
	double CM;
	ReferenceFrame2 ref;

	public Drive(double distance, double speed) {
		CM = distance - (5);
		this.speed = speed;
		ref = Robot.getInstance().referanceFrame2;
	}

	public Drive() {
		ref = Robot.getInstance().referanceFrame2;
		SmartDashboard.getNumber("Speed", 0);
		CM = 0;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		drive = Robot.getInstance().driveTrain;

		timer = new Timer();
		// CM = SmartDashboard.getNumber("Centimeters",0);
		// speed = SmartDashboard.getNumber("speed", 0);
		timer.reset();
		timer.start();
		ref.motorEncoder.reset();
		ref.reset();

	}

	@Override
	protected void execute() {

		drive.forwardWithGyro(speed);
		SmartDashboard.putNumber("GYRO", ref.getAngle());

		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		System.out.println("GotHere");
		System.out.println(Robot.getInstance().imu.getAngle());
		ref.DistanceTraveled();
		if (ref.DistanceTraveled() > CM) {

			return true;
		}

		else {
			return false;
		}
	}

	@Override
	protected void end() {
		drive.halt();
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
