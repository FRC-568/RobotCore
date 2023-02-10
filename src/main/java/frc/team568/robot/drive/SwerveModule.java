package frc.team568.robot.drive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface SwerveModule {

	/**
	 * Returns the motor controlling the module's drive wheel.
	 * 
	 * @return The drive motor.
	 */
	public abstract MotorController getDriveMotor();

	/**
	 * Returns the motor controlling the module's rotation.
	 * 
	 * @return The turning motor.
	 */
	public abstract MotorController getTurningMotor();

	/**
	 * Returns the total distance travelled by the wheel, measured in meters.
	 * 
	 * @return The wheel's distance.
	 */
	public abstract double getDistanceMeters();

	/**
	 * Returns the total rotation of the module, measured counter-clockwise.
	 * 
	 * @return The wheel's heading.
	 */
	public abstract Rotation2d getRotation();

	/**
	 * Returns the current wheel velocity in meters per second.
	 * 
	 * @return Wheel velocity.
	 */
	public abstract double getVelocityMetersPerSecond();

	/**
	 * Returns the rate of angular rotation in radians per second, measured counter-clockwise.
	 * 
	 * @return The angular rate.
	 */
	public abstract double getRotationRateRadians();

	/**
	 * Returns the current state of the module.
	 *
	 * @return The current state of the module.
	 */
	public default SwerveModuleState getState() {
		return new SwerveModuleState(getVelocityMetersPerSecond(), getRotation());
	}

	/**
	 * Returns the current position of the module.
	 *
	 * @return The current position of the module.
	 */
	public default SwerveModulePosition getPosition() {
		return new SwerveModulePosition(getDistanceMeters(), getRotation());
	}

	/**
	 * Sets the desired state for the module.
	 *
	 * @param desiredState Desired state with speed and angle.
	 */
	public abstract void setDesiredState(SwerveModuleState desiredState);

}
