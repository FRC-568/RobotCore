package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * An interface providing common controls for differential drive Subsystems.
 * 
 * <p>Differential drives travel only in the X (forward) axis and rotate in the Z (up) axis.
 */
public interface DifferentialDriveSubsystem extends Subsystem {
	public void arcadeDrive(double xSpeed, double zRotation);
	public void arcadeDrive(double xSpeed, double zRotation, boolean squareInputs);
	public void curvatureDrive(double xSpeed, double zRotation, boolean allowTurnInPlace);
	public void tankDrive(double leftSpeed, double rightSpeed);
	public void tankDrive(double leftSpeed, double rightSpeed, boolean squareInputs);
	public void stopMotor();
}