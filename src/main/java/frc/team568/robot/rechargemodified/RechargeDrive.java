package frc.team568.robot.rechargemodified;

import static frc.team568.robot.rechargemodified.DriveConstants.D_ANGLE;
import static frc.team568.robot.rechargemodified.DriveConstants.D_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.I_ANGLE;
import static frc.team568.robot.rechargemodified.DriveConstants.I_DRIVE;
import static frc.team568.robot.rechargemodified.DriveConstants.P_ANGLE;
import static frc.team568.robot.rechargemodified.DriveConstants.P_DRIVE;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.Limelight;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class RechargeDrive extends TwoMotorDrive {
	
	private boolean aimMode = false;

	// Subsystems
	private Limelight limelight;
	private Shooter shooter;

	// PID for angle
	PIDController pidAngle = new PIDController(P_ANGLE, I_ANGLE, D_ANGLE);
	PIDController pidDrive = new PIDController(P_DRIVE, I_DRIVE, D_DRIVE);

	public RechargeDrive(RobotBase robot) {

		super(robot);

	}

	public void getSubsystems(Limelight limelight, Shooter shooter) {

		this.limelight = limelight;
		this.shooter = shooter;

	}

	@Override
	public void update() {

		if (button("shootAim") && !aimMode) {

			aimMode = true;

		}

		if (aimMode) {

			overrrideMode = true;
			double distance = limelight.getDistance();
			double angle = limelight.getOffsetX();

			// Set goal distance
			double goalDistance;
			if (distance < 120) {

				goalDistance = 100;
				
			} else if (distance < 180) {

				goalDistance = 150;

			} else if (distance < 240) {

				goalDistance = 210;

			} else {

				goalDistance = 270;

			}

			// Setup PID
			pidAngle.setSetpoint(0);
			pidAngle.setTolerance(1);
			pidDrive.setSetpoint(goalDistance);
			pidDrive.setTolerance(1);

			// Set output limit
			double angleCorrection = MathUtil.clamp(pidAngle.calculate(angle), -1.0, 1.0);
			double driveCorrection = MathUtil.clamp(pidDrive.calculate(distance), -1.0, 1.0);

			// Calculate
			double powerL = angleCorrection + driveCorrection;
			double powerR = -angleCorrection + driveCorrection;

			// Set power
			leftMotor.set(powerL);
			rightMotor.set(powerR);

			// TODO: set shooter hood

			// Not in aimmode if reached target
			if (pidAngle.atSetpoint() && pidDrive.atSetpoint()) aimMode = false;

		} else {

			// Reset
			pidAngle.reset();
			pidDrive.reset();
			overrrideMode = false;

		}

	}

}
