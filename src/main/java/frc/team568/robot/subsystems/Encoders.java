/*
 * package frc.team568.robot.subsystems;
 * 
 * import frc.team568.robot.Robot;
 * 
 * import edu.wpi.first.wpilibj.Encoder;
 * 
 * public class Encoders { Encoder encoder; public static final double
 * radiusOfWheelInCM = 27.5; public static final double ticksPerRotation =
 * 1120.0;
 * 
 * public Encoders() { encoder = new Encoder(8, 9); }
 * 
 * public void driveForwardUsingEncoders(double centimeters, double speed) { if
 * (encoder.getDistance() < centimeters) {
 * Robot.getInstance().tankDrive.goForwards(speed); } else {
 * Robot.getInstance().tankDrive.halt(); } }
 * 
 * public int convertCMToTicks(double centimeters) { double result =
 * ticksPerRotation * centimeters / (2 * radiusOfWheelInCM * Math.PI); return
 * (int) result; }
 * 
 * }
 * 
 * public static final double ticksPerRotation = 1120.0; public static final
 * double radiusOfWheelInCM = 27.5; circumfrance of wheel=68 inches
 * 
 * public void driveForwardInCM(double powerLevel, double centimeters) {
 * applyPowerForTicks(true, powerLevel, convertCMToTicks(centimeters)); }
 * 
 * public void driveBackwardsInCM(double powerLevel, double centimeters) {
 * applyPowerForTicks(false, powerLevel, convertCMToTicks(centimeters)); }
 */
