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

	public static final int leftFrontMotor = 1;
	public static final int leftBackMotor = 2;
	public static final int rightFrontMotor = 3;
	public static final int rightBackMotor = 4;

	public static final int joy1Pos = 0;
	public static final int joy2Pos = 1;

	public static final int gearPneumatic1 = 0;
	public static final int gearPneumatic2 = 1;
	public static final int gearDetector = 7;
	public static final int laser1 = 5;
	public static final int encoderYellow = 0;
	public static final int encoderWhite = 1;

	public static final int ropeClampIn = 2;
	public static final int ropeClampOut = 6;
	// public static final int bottomClampIn = 2;
	public static final int reacherIn = 3;
	public static final int reacherOut = 4;
	public static final int bottomClampOut = 5;

	public static final int shooter = 6;
	public static final int gateServo = 7;
	public static final int climber = 5;
	public static final int ballWranglerIn = 3;
	public static final int ballWranglerOut = 4;

}
