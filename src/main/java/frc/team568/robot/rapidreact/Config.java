package frc.team568.robot.rapidreact;

final class Config {
	static final int kcompressor = 0;
	
	static final class MecanumSubsystem {
		static final int kMotorId_FR = 1;
		static final int kMotorId_BR = 2;
		static final int kMotorId_FL = 3;
		static final int kMotorID_BL = 4;
	}

	static final class Lift {
		static final int kUprightFlow = 3;
		static final int kSlantedFlow = 7;
		static final int kMotorId = 5;
	}

	static final class Intake {
		//in-flow side is the bottom of the piston
		static final int kLiftUp = 1;
		static final int kLiftDown = 5;
		static final int kLidOpen = 4;
		static final int kLidClosed = 0;
		static final int kMotorId = 6;
	}
}
