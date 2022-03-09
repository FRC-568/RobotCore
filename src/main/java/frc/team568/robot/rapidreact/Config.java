package frc.team568.robot.rapidreact;

final class Config {
	static final int kcompressor = 0;
	
	static final class MecanumSubsystem {
		static final int kmotorFR_ID = 1;
		static final int kmotorFL_ID = 2;
		static final int kmotorBR_ID = 3;
		static final int kmotorBL_ID = 4;
	}

	static final class Lift {
		static final int kLiftuprightFlow = 0;
		static final int kLiftslantedFlow = 1;
		static final int kmotorLift_ID = 5;
	}

	static final class Intake {
		static final int kintakeLiftUp = 2;
		static final int kintakeLiftDown = 3;
		static final int kintakeLidOpen = 4;
		static final int kintakeLidClosed = 5;
		static final int kmotorIntake_ID = 6;
	}
}
