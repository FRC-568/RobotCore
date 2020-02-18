package frc.team568.robot.recharge;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	RobotBase robot;
	Shooter shooter;
	TalonSRXDrive drive;
	Command autonomousCommand;

	public Robot() {
		super("Recharge");

		int mainController = 0;

		config("drive/leftMotors", new Integer[]{2, 1});
		config("drive/rightMotors", new Integer[] {4, 3});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);
		
		config("shooter/shooterL", 5);
		config("shooter/shooterR", 6);
		config("shooter/wheel", 7);
		config("shooter/rotator", 8);

		axis("forward", mainController, Xinput.LeftStickY);
		axis("turn", mainController, Xinput.RightStickX);
		axis("left", mainController, Xinput.LeftStickY);
		axis("right", mainController, Xinput.RightStickY);

		button("intake", mainController, Xinput.LeftBumper);
		button("shoot", mainController, Xinput.RightBumper);
		button("rotateShooterUp", mainController, Xinput.Y);
		button("rotateShooterDown", mainController, Xinput.A);

		shooter = addSubsystem(Shooter::new);
		drive = addSubsystem(TalonSRXDrive::new);

		new JoystickButton(new Joystick(mainController), Xinput.X).whenHeld(new ShooterAlignCommand(drive, robot)); //TODO check if this works

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
