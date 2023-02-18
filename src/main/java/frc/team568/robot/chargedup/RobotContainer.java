package frc.team568.robot.chargedup;

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
	final AprilTags tracker;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		drive = new SwerveSubsystem();
		drive.setDefaultCommand(new SwerveSubsystemDefaultCommand(drive));

		tracker = new AprilTags("photonvision", new Translation3d(0.0, 0.0, 0.0), new Rotation3d(0.0, 0.0, 0.0));

		lift = new LiftSubsystem(0, 0);

		configureButtonBindings();
	}

	public void configureButtonBindings() {
		controller1.povUp().onTrue(new InstantCommand(() -> lift.setLevel(3)));
		controller1.povRight().onTrue(new InstantCommand(() -> lift.setLevel(2)));
		controller1.povLeft().onTrue(new InstantCommand(() -> lift.setLevel(1)));
		controller1.povDown().onTrue(new InstantCommand(() -> lift.setLevel(0)));
		
		controller1.rightBumper().onTrue(new InstantCommand(() -> lift.set(0.2)));
		controller1.rightBumper().onFalse(new InstantCommand(() -> lift.set(0)));
		
		controller1.leftBumper().onTrue(new InstantCommand(() -> lift.set(-0.2)));
		controller1.leftBumper().onFalse(new InstantCommand(() -> lift.set(0)));
	}

	public Command getAutonomousCommand() {
		return Commands.none();
	}

}