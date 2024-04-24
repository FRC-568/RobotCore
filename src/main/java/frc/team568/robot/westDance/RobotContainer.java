package frc.team568.robot.westDance;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.team568.robot.westDance.subsystem.ArmSubsystem;
import frc.team568.robot.westDance.subsystem.DriveSubsystem;
import frc.team568.robot.westDance.subsystem.HeadSubsystem;

final class RobotContainer {
	CommandXboxController controller1;
	CommandXboxController controller2;
	final DriveSubsystem drive;
	final ArmSubsystem arms;
	final HeadSubsystem head;

	PowerDistribution pd;

	public RobotContainer() {
		controller1 = new CommandXboxController(0);
		controller2 = new CommandXboxController(1);

		drive = new DriveSubsystem();
		arms = new ArmSubsystem();
		head = new HeadSubsystem();

		configureButtonBindings();

		pd = new PowerDistribution(1, ModuleType.kRev);
	}

	public void configureButtonBindings() {
		controller1.leftStick().whileTrue(
			new RunCommand(
				() ->
					drive.setSpeed(
						controller1.getLeftY(),
						controller1.getRightX()
					), 
				drive)
		);
		arms.setDefaultCommand(new RunCommand(
			() -> arms.setSpeed(
				-controller2.getLeftY(), 
				-controller2.getRightY()
			), arms
		));

		head.setDefaultCommand(
			new RunCommand(
			() -> head.setSpeed(
				controller2.getRightTriggerAxis() - controller2.getLeftTriggerAxis()
			),
			head
			)

		);		
	}

}
