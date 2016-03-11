package org.usfirst.frc.team568.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;

	public static final int nudge = 8;
	public static final int leftFrontMotor = 4;
	public static final int leftBackMotor = 5;
	public static final int rightFrontMotor = 1;
	public static final int rightBackMotor = 2;

	public static final int upperLimmitSwitch = 3;
	public static final int lowerLimmitSwitch = 2;
	public static final int topLimmitSwitch = 0;
	public static final int bottomLimmitSwitch = 1;

	public static final int Spike2 = 1;
	public static final int Spike1 = 0;
	// public static final int lifterMotor = 5;
	// public static final int lowZoneMotor = 8;

	public static final int joy1Pos = 0;
	public static final int joy2Pos = 1;
	public static final int joy3Pos = 2;
	/*
	 * public static final int aimerUp = 0; public static final int aimerDown =
	 * 1; public static final int brake = 2;
	 */
	public static final int shooterLeftPort = 0;
	public static final int shooterRightPort = 9;
	public static final int leftTiltPort = 6;
	public static final int rightTiltPort = 7;

	// public static final int rightMidMotor = 5;
	// public static final int leftMidMotor = 2;
	public class CrateBot {
		public static final int leftFront = 0;
		public static final int leftBack = 1;
		public static final int rightFront = 2;
		public static final int rightBack = 4;

		public static final int brake = 2;
		public static final int lifterMotor = 5;
		public static final int lowZoneMotor = 8;

	}
}
