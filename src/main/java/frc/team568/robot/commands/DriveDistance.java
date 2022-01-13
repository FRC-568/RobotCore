package frc.team568.robot.commands;

import static frc.team568.robot.rechargemodified.DriveConstants.D_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.GEAR_RATIO;
import static frc.team568.robot.rechargemodified.DriveConstants.I_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.P_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.TPR;
import static frc.team568.robot.rechargemodified.DriveConstants.WHEEL_CIRCUMFERENCE;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.MathUtil;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class DriveDistance extends CommandBase {

	private final TwoMotorDrive drive;
	private final double speed;
	private final double distanceL;
	private final double distanceR;

	private final PIDController pidSpeedL = new PIDController(P_DRIVE, I_DRIVE, D_DRIVE);
	private final PIDController pidSpeedR = new PIDController(P_DRIVE, I_DRIVE, D_DRIVE);
	private final double TOLERANCE = 2000;

	public DriveDistance(TwoMotorDrive drive, double distanceL, double distanceR, double speed) {

		this.drive = drive;
		this.speed = speed;
		this.distanceL = distanceL;
		this.distanceR = distanceR;

	}

	@Override
	public void initialize() {

		// Reset robot
		//drive.resetGyro();
		drive.resetMotors();
		pidSpeedL.reset();
		pidSpeedR.reset();

		// Calculate goal ticks
		double goalTicksL = (distanceL * TPR * GEAR_RATIO) / WHEEL_CIRCUMFERENCE;
		double goalTicksR = (distanceR * TPR * GEAR_RATIO) / WHEEL_CIRCUMFERENCE;

		// Set setpoint of encoder ticks
		pidSpeedL.setSetpoint(goalTicksL * -1);
		pidSpeedR.setSetpoint(goalTicksR * -1);

		// Set tolerances of PID controllers
		pidSpeedL.setTolerance(TOLERANCE);
		pidSpeedR.setTolerance(TOLERANCE);

	}

	@Override
	public void execute() {

		double ratio = 1;
		if (distanceL > 0 && distanceR > 0)
			ratio = distanceL / distanceR;

		// Calculate motor power
		double powerL = MathUtil.clamp(pidSpeedL.calculate(drive.getLeftPos()), -speed * ratio, speed * ratio);
		double powerR = MathUtil.clamp(pidSpeedR.calculate(drive.getRightPos()), -speed, speed);

		
		// Set motor powers
		drive.setLeft(powerL);
		drive.setRight(powerR);
	
	}

	@Override
	public boolean isFinished() {

		return pidSpeedL.atSetpoint() && pidSpeedR.atSetpoint();
	
	}

	@Override
	public void end(boolean interrupted) {

		// Reset robot
		drive.stop();
		//drive.resetGyro();
		drive.resetMotors();
		pidSpeedL.reset();
		pidSpeedR.reset();

		// Wait for 0.5 seconds
		final Timer timer = new Timer();
		timer.reset();
		timer.start();
		while (!timer.hasElapsed(0.5)) {}
		timer.stop();
	
	}

}
