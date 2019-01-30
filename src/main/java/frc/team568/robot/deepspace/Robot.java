package frc.team568.robot.deepspace;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team568.robot.RobotBase;

public class Robot extends RobotBase {
	Command autonomousCommand;

	private Compressor compressor;

	public Timer timer;

	DriveTrain2019 driveTrain;

	Camera camera;

	// Camera server port
	private final int cameraServerPort = 0;

	public Robot() {
		// Joystick ports
		port("mainJoystick", 1);
		port("cameraJoystick", 0);

		// Motor ports
		port("leftFrontMotor", 1);
		port("leftBackMotor", 2);
		port("rightFrontMotor", 3);
		port("rightBackMotor", 4);

		// Servo ports
		port("verticalServo", 0);
		port("horizontalServo", 1);

		// Double solenoid ports
		port("forwardChannel", 0);
		port("reverseChannel", 1);

		// Camera
		camera = addSubsystem(Camera::new);

		// Drivetrain
		driveTrain = addSubsystem(DriveTrain2019::new);

		// Compressor
		compressor = new Compressor();
	}

	@Override
	public void robotInit() {
		compressor.start();
		CameraServer.getInstance().startAutomaticCapture(cameraServerPort);
	}

	@Override
	public void robotPeriodic() {

	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

	}

	@Override
	// Called every 20 milliseconds in teleop
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			autonomousCommand = null;
		}
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
}
