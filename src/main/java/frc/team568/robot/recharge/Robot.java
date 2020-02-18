package frc.team568.robot.recharge;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	public TalonSRXDrive drive;
	private Command autonomousCommand;

	public Robot() {
		super("Recharge");

		port("mainJoystick", 0);

		config("drive/leftMotors", new Integer[]{0, 1});
		config("drive/rightMotors", new Integer[] {2, 3});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);

		axis("forward", 0, Xinput.LeftStickY);
		axis("turn", 0, Xinput.RightStickX);
		axis("left", 0, Xinput.LeftStickY);
		axis("right", 0, Xinput.RightStickY);

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
		
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

}
