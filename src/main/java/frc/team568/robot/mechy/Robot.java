package frc.team568.robot.mechy;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.MechyDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Robot extends RobotBase {
	
	public MechyDrive drive;
	private Command autonomousCommand;

	public Robot() {
		super("Mechy");

		int mainJoystick = 0;

		JoystickButton joystickStart = new JoystickButton(new Joystick(mainJoystick), Xinput.Start);

		port("leftFrontMotor", 1);
		port("rightFrontMotor", 2);
		port("leftBackMotor", 3);
		port("rightBackMotor", 4);

		axis("forward", mainJoystick, Xinput.LeftStickY);
		axis("side", mainJoystick, Xinput.LeftStickX);
		axis("turn", mainJoystick, Xinput.RightStickX);

		joystickStart.whenPressed(new InstantCommand(() -> {

			drive.drivePOV = !drive.drivePOV;
			drive.gyro.reset();

		}));
		button("driveModeToggle", mainJoystick, Xinput.Start);
		button("safeModeToggle", () -> button(0, Xinput.LeftStickIn) && button(0, Xinput.RightStickIn));

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
