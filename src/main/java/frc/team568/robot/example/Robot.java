package frc.team568.robot.example;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.SimpleDrive;

public class Robot extends RobotBase {
	
	SimpleDrive drive;
	final int drivingControllerPort = 0;
	XinputController driverController = new XinputController(drivingControllerPort);

	public Robot() {

		// Name of the robot
		super("Drive");

		// Mapping the ports
		port("leftMotor", 0);
		port("rightMotor", 1);

		// Mapping the controls
		axis("forward", drivingControllerPort, Xinput.LeftStickY);
		axis("turn", drivingControllerPort, Xinput.RightStickX);

		// Subsystems
		drive = new SimpleDrive(this);

	}

	@Override
	public void teleopInit() {
		drive.initDefaultCommand();
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
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

}
