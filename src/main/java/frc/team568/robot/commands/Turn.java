package frc.team568.robot.commands;

import static frc.team568.robot.rechargemodified.DriveConstants.D_TURN;
import static frc.team568.robot.rechargemodified.DriveConstants.I_TURN;
import static frc.team568.robot.rechargemodified.DriveConstants.P_TURN;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Turn extends CommandBase {
	
	private final TwoMotorDrive drive;
	private final double angle;
	private final double speed;

	private final PIDController pidTurn = new PIDController(P_TURN, I_TURN, D_TURN);
	private final double TOLERANCE = 1;

	public Turn(TwoMotorDrive drive, double angle, double speed) {

		this.drive = drive;
		this.angle = angle;
		this.speed = speed;

	}

	@Override
	public void initialize() {
		
		// Reset robot
		drive.resetGyro();
		drive.resetMotors();
		pidTurn.reset();

		// Set setpoint of encoder ticks
		pidTurn.setSetpoint(angle);

		// Set tolerances of PID controllers
		pidTurn.setTolerance(TOLERANCE);

	}

	@Override
	public void execute() {

		// Calculate motor power
		double power = MathUtil.clamp(pidTurn.calculate(drive.getAngle()), -speed, speed);

		// Set motor powers
		drive.setLeft(power);
		drive.setRight(-power);
	
	}

	@Override
	public boolean isFinished() {

		return pidTurn.atSetpoint();
	
	}

	@Override
	public void end(boolean interrupted) {

		drive.stop();
		drive.resetGyro();
		drive.resetMotors();
		pidTurn.reset();
	
	}

}
