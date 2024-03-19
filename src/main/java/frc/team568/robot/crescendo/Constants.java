package frc.team568.robot.crescendo;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.I2C;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public final class Constants {

	public static final class OIConstants {

		public static final int kDriverControllerPort = 0;
		public static final int kCopilotControllerPort = 1;
		public static final double kControllerDeadband = 0.05; // inputs below are ignored; above are re-scaled from
																// deadband offset.
		public static final double kAxisSlewRate = 5; // Controller axis max rate of change in units per second.

	}

	public static final class SwerveConstants {

		public static final int kEncoderResolution = 4096; // counts per rotation of turning motor
		public static final double kRelativeEncoderResolution = 42;
		public static final double kWheelRadius = 0.0508; // 4 inch wheels on mk4i
		public static final double kWheelCircumference = 2 * Math.PI * kWheelRadius;
		public static final double kTurnGearRatio = 150.0 / 7.0;
		public static final double kDriveGearRatio = 8.14;
		public static final double kMaxSpeed = 3.4; // 3.2 meters per second - 2.5 is probably more reliable / safe
		public static final double kMaxDriveRpm = kMaxSpeed / kWheelCircumference * 60; // Max drive speed in RPM
		public static final double kMaxDriveAcceleration = kMaxDriveRpm / 0.15; // Max velocity after 1s
		public static final double kMaxRampRate = 0.15;
		public static final double kMaxSpinRate = 4 * Math.PI; // 2 rotations per second
		public static final double kModuleMaxAngularVelocity = 32 * Math.PI; // 2 rotations per second
		public static final double kModuleMaxAngularAcceleration = 32 * Math.PI; // radians per second squared
		public static final double kWheelbaseRadius = 0.78105;
		public static final int kDrivePidChannel = 0;
		public static final double kSlowMultiplier = 0.25;
		public static final double kNormalMultiplier = 1.0;
		public static final PathConstraints kPathFollowerConstraints = new PathConstraints(3.0, 3.0,
				Units.degreesToRadians(540), Units.degreesToRadians(720));

	}

	public static final class PivotConstants {

		public static final String kCANBusName = "roborio";
		public static final int kLeftMotorPort = 14;
		public static final int kRightMotorPort = 15;
		public static final double kMinAngle = 0.0;
		public static final double kMaxAngle = 90.0;
		public static final double kG = 0.1;
		public static final Slot0Configs kPidConstants = 
			new Slot0Configs()
			.withKS(0)
			.withKV(0.0) // 12/145 = 0.08
			.withKA(0)
			.withKP(0.05)
			.withKI(0)
			.withKD(0);
		
		public static final MotionMagicConfigs kMotionMagicConfigs = 
			new MotionMagicConfigs()
			.withMotionMagicCruiseVelocity(PivotSubsystem.degToRot(90))
			.withMotionMagicAcceleration(PivotSubsystem.degToRot(90));

	}

	public static final class JukeboxConstants {

		public static final String kCANBusName = "canivore";
		public static final int kIntakePort = 0;
		public static final int kLeftOuttakePort = 12;
		public static final int kRightOuttakePort = 11;
		public static final I2C.Port kNoteDetectorPort = I2C.Port.kOnboard;
		public static final double kNoteDetectionRange = 350;
		public static final double kOuttakeMinRPS = 5.0;
		public static final double kOuttakeMaxRPS = 96.5; // previously 80.0;
		public static final double kIntakePower = 1.0;
		public static final Slot0Configs kOuttakePID = new Slot0Configs()
				.withKS(0.24)
				.withKV(0.114)
				.withKP(0.5) // An error of 0.5 rotations and a value of 24 results in 12 V output
				.withKI(0) // no output for integrated error
				.withKD(0); // A velocity of 1 rps results in 0.1 V output at a setting of 0.1

	}

	public static final class VisionConstants {

		public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
		public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);

	}
}
