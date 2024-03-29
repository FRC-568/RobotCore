package frc.team568.robot.swervebot;

import static frc.team568.robot.swervebot.Constants.OIConstants.kAxisSlewRate;
import static frc.team568.robot.swervebot.Constants.OIConstants.kControllerDeadband;
import static frc.team568.robot.swervebot.Constants.OIConstants.kCopilotControllerPort;
import static frc.team568.robot.swervebot.Constants.OIConstants.kDriverControllerPort;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

final class OI {
	static final CommandXboxController driverController = new CommandXboxController(kDriverControllerPort);
	static final CommandXboxController copilotController = new CommandXboxController(kCopilotControllerPort);

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
					* Math.pow(Constants.SwerveConstants.kMaxSpinRate / 4,2);
		}
	}

	static final class Button {
		public static final Trigger fieldRelativeControl = driverController.start();
	}
}
