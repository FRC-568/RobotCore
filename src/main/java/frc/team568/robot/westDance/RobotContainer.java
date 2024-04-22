package frc.team568.robot.westDance;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.team568.robot.westDance.subsystem.DriveSubsystem;

final class RobotContainer {
	CommandXboxController controller1;
	CommandXboxController controller2;
	final DriveSubsystem drive;

	PowerDistribution pd;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);

		drive = new DriveSubsystem();

		configureButtonBindings();

		pd = new PowerDistribution(1, ModuleType.kRev);
	}

	public void configureButtonBindings() {
		// controller1.leftBumper().whileTrue(new Intake(jukebox, pivot)); //intake until bumper is released
		// controller1.b().onTrue(new ScoreAmp(jukebox, pivot));
		// controller1.x().onTrue(new ScoreSpeaker(jukebox, pivot));
		// controller1.y().onTrue(new InstantCommand(() -> pivot.setAngle(0)));
		// controller1.a().onTrue(new InstantCommand(() -> pivot.setAngle(90)));
		// controller1.rightTrigger().whileTrue(Commands.runEnd(() -> new Command(jukebox.setOuttakeSpeed(controller1.getRightTriggerAxis(),controller1.getRightTriggerAxis())),() ->new Command(jukebox.setOuttakeSpeed(0,0))));
		// controller1.leftTrigger().whileTrue(Commands.runEnd(() -> new Command(jukebox.setIntakeSpeed(controller1.getLeftTriggerAxis())),() ->new Command(jukebox.setIntakeSpeed(0))));
		
	}

}
