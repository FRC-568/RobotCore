package frc.team568.robot.bart;

import static edu.wpi.first.wpilibj.XboxController.Button.kBack;
import static edu.wpi.first.wpilibj.XboxController.Button.kStart;
import static edu.wpi.first.wpilibj.XboxController.Button.kLeftStick;
import static edu.wpi.first.wpilibj.XboxController.Button.kRightStick;

import java.util.Map;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.PanTiltCamera;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	public TalonSRXDrive drive;
	public PanTiltCamera camera;
	private Command autonomousCommand;
	XinputController driver = new XinputController(0);

	public Robot() {
		super("Bart");

		config("drive/leftMotors", new Integer[]{15, 14});
		config("drive/rightMotors", new Integer[] {13, 12});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);

		driver.getButton(kBack).onTrue(new InstantCommand(drive::toggleIsReversed));
		driver.getButton(kStart).onTrue(new InstantCommand(drive::toggleTankControls));
		driver.getButton(kLeftStick)
			.and(driver.getButton(kRightStick))
			.debounce(5)
			.onTrue(new InstantCommand(drive::toggleSafeMode));

		drive = new TalonSRXDrive(this);
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driver.getLeftY(),
			Input.TURN, () -> driver.getRightX(),
			Input.TANK_LEFT, () -> -driver.getLeftY(),
			Input.TANK_RIGHT, () -> -driver.getRightY()
		)));
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
		//autonomousCommand = new DriveForward(driveTrain, 5);
		// driveTrain.driveDist(24); <- This is for testing
		//autonomousCommand.start();
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}
}
