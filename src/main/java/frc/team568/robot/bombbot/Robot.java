package frc.team568.robot.bombbot;

import java.util.Map;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {

	TalonSRXDrive drive;
	XinputController driverController;

	public Robot() {
		super("BombBot");

		config("drive/leftMotors", new Integer[] {1});
		config("drive/rightMotors", new Integer[] {2});
		config("drive/leftInverted", false);
		config("drive/rightInverted", false);

		drive = new TalonSRXDrive(this);
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driverController.getLeftY(),
			Input.TURN, () -> driverController.getRightX(),
			Input.TANK_LEFT, () -> -driverController.getLeftY(),
			Input.TANK_RIGHT, () -> -driverController.getRightY()
		)));
	}

	public void robotInit() {
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void disabledInit() {
	}
}