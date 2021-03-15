package frc.team568.robot.commands;

import static frc.team568.robot.rechargemodified.DriveConstants.D_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.I_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.P_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.TPR;
import static frc.team568.robot.rechargemodified.DriveConstants.WHEEL_CIRCUMFERENCE;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class DriveDistance extends CommandBase {
	
	private final TwoMotorDrive drive;
	private final double speed;
	private final double distanceL;
	private final double distanceR;

	private final PIDController pidSpeedL = new PIDController(P_DRIVE, I_DRIVE, D_DRIVE);
	private final PIDController pidSpeedR = new PIDController(P_DRIVE, I_DRIVE, D_DRIVE);
	private final double TOLERANCE = 50;

	public DriveDistance(TwoMotorDrive drive, double distanceL, double distanceR, double speed) {

		this.drive = drive;
		this.speed = speed;
		this.distanceL = distanceL;
		this.distanceR = distanceR;

	}

	@Override
	public void initialize() {
		
		// Reset robot
		drive.resetGyro();
		drive.resetMotors();
		pidSpeedL.reset();
		pidSpeedR.reset();

		// Calculate goal ticks
		double goalTicksL = (distanceL * TPR) / WHEEL_CIRCUMFERENCE;
		double goalTicksR = (distanceR * TPR) / WHEEL_CIRCUMFERENCE;

		// Set setpoint of encoder ticks
		pidSpeedL.setSetpoint(goalTicksL);
		pidSpeedR.setSetpoint(goalTicksR);

		// Set tolerances of PID controllers
		pidSpeedL.setTolerance(TOLERANCE);
		pidSpeedR.setTolerance(TOLERANCE);

	}

	@Override
	public void execute() {

		// Calculate motor power
		double powerL = MathUtil.clamp(pidSpeedL.calculate(drive.getLeftPos()), -speed, speed);
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

		drive.stop();
		drive.resetGyro();
		drive.resetMotors();
		pidSpeedL.reset();
		pidSpeedR.reset();
	
	}

}
