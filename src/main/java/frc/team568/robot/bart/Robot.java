package frc.team568.robot.bart;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.GyroSubsystem;
import frc.team568.robot.subsystems.PanTiltCamera;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	public TalonSRXDrive drive;
	public PanTiltCamera camera;
	private Command autonomousCommand;

	public Robot() {
		super("Bart");

		final int mainControllerPort = 0;

		config("drive/leftMotors", new Integer[]{1, 2});
		config("drive/rightMotors", new Integer[] {3, 4});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);

		axis("forward", mainControllerPort, Xinput.LeftStickY);
		axis("turn", mainControllerPort, Xinput.RightStickX);
		axis("left", mainControllerPort, Xinput.LeftStickY);
		axis("right", mainControllerPort, Xinput.RightStickY);

		button("driveReverse", 0, Xinput.Back);
		button("tankModeToggle", 0, Xinput.Start);
		button("safeModeToggle", () -> button(mainControllerPort, Xinput.LeftStickIn) && button(mainControllerPort, Xinput.RightStickIn));

		addSubsystem(GyroSubsystem::new);
		drive = addSubsystem(TalonSRXDrive::new);

	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		drive.resetSensors();
		drive.resetGyro();
	}

	@Override
	public void teleopPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		CommandScheduler.getInstance().run();
		
	}

	@Override
	public void disabledInit() {
		
	}

	@Override
	public void autonomousInit() {
		//autonomousCommand = new DriveForward(driveTrain, 5);
		// driveTrain.driveDist(24); <- This is for testing
		//autonomousCommand.start();
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

}
