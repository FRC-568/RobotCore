package frc.team568.robot.mechy;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.MechyDrive;

public class Robot extends RobotBase {
	
	public MechyDrive drive;
	private Command autonomousCommand;

	public Robot() {
		super("Mechy");

		port("mainJoystick", 0);

		port("leftFrontMotor", 1);
		port("rightFrontMotor", 2);
		port("leftBackMotor", 3);
		port("rightBackMotor", 4);

		axis("forward", 0, Xinput.LeftStickY);
		axis("side", 0, Xinput.LeftStickX);
		axis("turn", 0, Xinput.RightStickX);

		drive = addSubsystem(MechyDrive::new);

	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
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

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

}
