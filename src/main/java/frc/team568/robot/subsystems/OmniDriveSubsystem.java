package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * An interface providing common controls for omni-directional drive Subsystems.
 * 
 * <p>Omni-directional drives can move in the X (forward) and Y (left) axis, and rotate in the Z (up) axis.
 * 
 * <p>Common omni-drive setups include Mecanum, Swerve, and Killough drive.
 */
public interface OmniDriveSubsystem extends Subsystem {
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation);
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle);
	public void drivePolar(double magnitude, double angle, double zRotation);
	public void stopMotor();
}