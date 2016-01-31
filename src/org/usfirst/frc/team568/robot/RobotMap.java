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

	public static final int leftShooterMotor = 5;
	public static final int rightShooterMotor = 6;
	public static final int nudge = 7;
	public static final int leftFrontMotor = 0;
	public static final int leftBackMotor = 1;
	public static final int rightFrontMotor = 2;
	public static final int rightBackMotor = 4;
	public static final int lowZoneMotor = 8;

	public static final int joy1Pos = 0;
	public static final int joy2Pos = 1;

	public static final int aimerUp = 0;
	public static final int aimerDown = 1;

	// public static final int rightMidMotor = 5;
	// public static final int leftMidMotor = 2;

}
