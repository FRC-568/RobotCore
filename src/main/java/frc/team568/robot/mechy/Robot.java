package frc.team568.robot.mechy;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.MechyDrive;

public class Robot extends RobotBase {
	
	public MechyDrive drive;
	private Command autonomousCommand;

	public Robot() {
		super("Mechy");

		int mainJoystick = 0;

		port("leftFrontMotor", 1);
		port("rightFrontMotor", 2);
		port("leftBackMotor", 3);
		port("rightBackMotor", 4);

		axis("forward", mainJoystick, Xinput.LeftStickY);
		axis("side", mainJoystick, Xinput.LeftStickX);
		axis("turn", mainJoystick, Xinput.RightStickX);

		button("driveModeToggle", mainJoystick, Xinput.Start);

		drive = addSubsystem(MechyDrive::new);

	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
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
