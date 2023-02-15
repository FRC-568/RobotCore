package frc.team568.robot.chargedup;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;

import static frc.team568.robot.chargedup.Constants.OIConstants.*;

import java.util.function.DoubleSupplier;

final class OI {
	static final XboxController driverController = new XboxController(kDriverControllerPort);

	static final class Axis {
		public static final DoubleSupplier swerveForward;
		public static final DoubleSupplier swerveLeft;
		public static final DoubleSupplier swerveCCW;

		static {
			// Slew rate limiters to make joystick inputs more gentle; 1/x sec from 0 to 1.
			var xspeedLimiter = new SlewRateLimiter(kAxisSlewRate);
			var yspeedLimiter = new SlewRateLimiter(kAxisSlewRate);
			var m_rotLimiter = new SlewRateLimiter(kAxisSlewRate);

			swerveForward = () -> -xspeedLimiter
					.calculate(MathUtil.applyDeadband(driverController.getLeftY(), kControllerDeadband))
					* Constants.SwerveConstants.kMaxSpeed;
			swerveLeft = () -> -yspeedLimiter
					.calculate(MathUtil.applyDeadband(driverController.getLeftX(), kControllerDeadband))
					* Constants.SwerveConstants.kMaxSpeed;
			swerveCCW = () -> -m_rotLimiter
					.calculate(MathUtil.applyDeadband(driverController.getRightX(), kControllerDeadband))
					* Constants.SwerveConstants.kModuleMaxAngularVelocity;
		}
	}

}
