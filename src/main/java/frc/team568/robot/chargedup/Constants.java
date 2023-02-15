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

	}

}
