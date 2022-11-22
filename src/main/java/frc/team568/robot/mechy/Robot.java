package frc.team568.robot.mechy;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.MechyDrive;

public class Robot extends RobotBase {
	
	public MechyDrive drive;
	private Command autonomousCommand;

	public Robot() {
		super("Mechy");

		int mainJoystick = 0;

		JoystickButton joystickStart = new JoystickButton(new Joystick(mainJoystick), Xinput.Start);

		port("leftFrontMotor", 1);
		port("rightFrontMotor", 3);
		port("leftBackMotor", 2);
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

		drive = new MechyDrive(this);

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
