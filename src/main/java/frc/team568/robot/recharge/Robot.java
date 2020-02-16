package frc.team568.robot.recharge;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.TalonSRXDrive;
import frc.team568.robot.recharge.Shooter;

public class Robot extends RobotBase {
	RobotBase robot;
	Shooter shooter;
	TalonSRXDrive drive;
	Joystick controller0;
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
		shooter = addSubsystem(Shooter::new);
		drive = addSubsystem(TalonSRXDrive::new);

		//new JoystickButton(controller0, Xinput.X).whileActive(new ShooterAlignCommand(drive, robot)); //don't know why this command isn't working

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
