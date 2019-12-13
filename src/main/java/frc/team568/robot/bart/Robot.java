package frc.team568.robot.bart;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
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

		port("horizontalServo", 1);
		port("verticalServo", 0);

		port("cameraJoystick", 0);
		port("mainJoystick", 1);

		config("drive/leftMotors", new Integer[]{1, 2});
		config("drive/rightMotors", new Integer[] {3, 4});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);

		axis("forward", 0, Xinput.LeftStickY);
		axis("turn", 0, Xinput.RightStickX);
		axis("left", 0, Xinput.LeftStickY);
		axis("right", 0, Xinput.RightStickY);

		addSubsystem(GyroSubsystem::new);
		drive = addSubsystem(TalonSRXDrive::new);
		//camera = new PanTiltCamera(this);
		//addSubsystem(PanTiltCamera.class, camera);
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		drive.resetSensors();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
		
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
		Scheduler.getInstance().run();
	}

}
