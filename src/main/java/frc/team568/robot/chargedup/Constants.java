package frc.team568.robot.chargedup;

final class Constants {

	static final class OIConstants {
		
		static final int kDriverControllerPort = 0;
		static final int kCopilotControllerPort = 1;
		static final double kControllerDeadband = 0.05; // inputs below are ignored; above are re-scaled from deadband offset.
		static final double kAxisSlewRate = 10; // Controller axis max rate of change in units per second.

	}

	static final class SwerveConstants {

		static final int kEncoderResolution = 4096; // counts per rotation of turning motor
		static final double kRelativeEncoderResolution = 42;
		static final double kWheelRadius = 0.0508; // 4 inch wheels on mk4i
		static final double kWheelCircumference = 2 * Math.PI * kWheelRadius;
		static final double kTurnGearRatio = 150.0 / 7.0;
		static final double kDriveGearRatio = 8.14;
		static final double kMaxSpeed = 3.4; // 3.2 meters per second - 2.5 is probably more reliable / safe
		static final double kMaxDriveRpm = kMaxSpeed / kWheelCircumference * 60; // Max drive speed in RPM
		static final double kMaxDriveAcceleration = kMaxDriveRpm / 0.15; // Max velocity after 1s
		static final double kMaxRampRate = 0.15;
		static final double kMaxSpinRate = 4 * Math.PI; // 2 rotations per second
		static final double kModuleMaxAngularVelocity = 32 * Math.PI; // 2 rotations per second
		static final double kModuleMaxAngularAcceleration = 32 * Math.PI; // radians per second squared
		static final int kDrivePidChannel = 0;
		
		static final double kFrontOffset = 0.3849685;
		static final double kLeftOffset = 0.38735;
		static final double kRightOffset = 0.38735;
		static final double kBackOffset = 0.3849685;

		static final double kFrontRot = 5;
		static final double kLeftRot = -35;
		static final double kRightRot = 105;
		static final double kBackRot = 307;

	}

}
