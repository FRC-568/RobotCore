package frc.team568.robot.bart;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team568.robot.PMW3901;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.Locator;
import frc.team568.robot.subsystems.PanTiltCamera;
import frc.team568.util.Vector2;

public class Robot extends RobotBase {
	public WestCoastDrive driveTrain;
	public PMW3901 flow;
	public Locator locator;
	public PanTiltCamera camera;
	private Command autonomousCommand;

	public Robot() {
		super("bart");

		port("horizontalServo", 1);
		port("verticalServo", 0);

		port("cameraJoystick", 0);
		port("mainJoystick", 1);

		port("leftFrontMotor", 1);
		port("leftBackMotor", 2);
		port("rightFrontMotor", 3);
		port("rightBackMotor", 4);

		driveTrain = new WestCoastDrive(this);
		locator = new Locator(this);
		addSubsystem(Locator.class, locator);
		//camera = new PanTiltCamera(this);
		//addSubsystem(PanTiltCamera.class, camera);
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		if (flow != null)
			flow.startAutoLoop();
		driveTrain.reset();
	}

	@Override
	public void teleopPeriodic() {
		// locator.update();
		Scheduler.getInstance().run();
		if (flow != null) {
			Vector2 postition = flow.getPosition();
			System.out.println("X " + postition.x + " Y " + postition.y);
		}
	}

	@Override
	public void disabledInit() {
		if (flow != null)
			flow.stopAutoLoop();
	}

	@Override
	public void autonomousInit() {
		autonomousCommand = new DriveForward(driveTrain, 5);
		// driveTrain.driveDist(24, 1); <- This is for testing
		autonomousCommand.start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

}
