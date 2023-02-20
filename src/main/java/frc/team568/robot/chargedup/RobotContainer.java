package frc.team568.robot.chargedup;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.team568.robot.subsystems.AprilTags;

final class RobotContainer {
	CommandXboxController controller1;
	final SwerveSubsystem drive;
	final LiftSubsystem lift;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		// WARNING: this pose is empty
		drive = new SwerveSubsystem(new Pose2d());
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));

		lift = new LiftSubsystem(0, 0, 0, 0);

		configureButtonBindings();
	}

	public void configureButtonBindings() {
		controller1.povUp().onTrue(new InstantCommand(() -> lift.setLevel(3)));
		controller1.povRight().onTrue(new InstantCommand(() -> lift.setLevel(2)));
		controller1.povLeft().onTrue(new InstantCommand(() -> lift.setLevel(1)));
		controller1.povDown().onTrue(new InstantCommand(() -> lift.setLevel(0)));
		
		controller1.rightBumper().onTrue(new InstantCommand(() -> lift.set(0.1)));
		controller1.rightBumper().onFalse(new InstantCommand(() -> lift.set(0)));
		
		controller1.leftBumper().onTrue(new InstantCommand(() -> lift.set(-0.1)));
		controller1.leftBumper().onFalse(new InstantCommand(() -> lift.set(0)));
	}

	public Command getAutonomousCommand() {
		return Commands.none();
	}

}