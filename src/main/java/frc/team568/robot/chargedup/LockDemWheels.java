package frc.team568.robot.chargedup;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LockDemWheels extends CommandBase {
	SwerveSubsystem drive;
	boolean done = false;
	SwerveModuleState[] position = { new SwerveModuleState(0, new Rotation2d(Math.PI/2)),
									 new SwerveModuleState(0, new Rotation2d(0)),
									 new SwerveModuleState(0, new Rotation2d(Math.PI/2)),
									 new SwerveModuleState(0, new Rotation2d(0)) };

	LockDemWheels(SwerveSubsystem drive) {
		addRequirements(drive);
		this.drive = drive;
	}

	@Override
	public void initialize() { }

	@Override
	public void execute() {
		drive.setModuleStates(position);
		done = true;
	}

	@Override
	public boolean isFinished() {
		return done;
	}

	@Override
	public void end(boolean interrupted) { }
}
