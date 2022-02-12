package frc.team568.robot.deepspace;

final class Config {

	static final class BlinkinLights {
		static final int kControlPort = 8;
	}

	static final class Claw {
		static final int kOpenPort = 4;
		static final int kClosePort = 3;
	}

	static final class HabitatClimber {
		static final int kDrivePort = 8;
		static final int kFrontPort = 6;
		static final int kBackPort = 7;
	}

	static final class Lift {
		static final int kMotorPort = 5;
		static final int kHomeSwitchPort = 0;
	}

	static final class Shifter {
		static final int kLowSolenoidPort = 0;
		static final int kHighSolenoidPort = 3;
	}

	static final class Shpaa {
		static final int kExtenderOutPort = 7;
		static final int kExtenderInPort = 1;
		static final int kGrabberOpenPort = 2;
		static final int kGrabberClosePort = 6;
	}

}
