package frc.team568.robot.chargedup;

final class Constants {

	static final class OIConstants {
		
		static final int kDriverControllerPort = 0;
		static final int kCopilotControllerPort = 1;
		static final double kControllerDeadband = 0.02; // inputs below are ignored; above are re-scaled from deadband offset.
		static final double kAxisSlewRate = 3; // Controller axis max rate of change in units per second.

	}

	static final class SwerveConstants {

		static final int kEncoderResolution = 4096; // counts per rotation of turning motor
		static final double kWheelRadius = 0.047625; // 3.75 inch wheels on mk4i
		static final double kMaxSpeed = 3.0; // 3 meters per second
		static final double kModuleMaxAngularVelocity = Math.PI; // 1/2 rotation per second
		static final double kModuleMaxAngularAcceleration = 2 * Math.PI; // radians per second squared
		
		static final double kFrontOffset = 0.3849685;
		static final double kLeftOffset = 0.38735;
		static final double kRightOffset = 0.38735;
		static final double kBackOffset = 0.3849685;

		static final double kFrontRot = 3.26589;
		static final double kLeftRot = 28.81978;
		static final double kRightRot = 234.2251;
		static final double kBackRot = 52.9986;

	}

}
