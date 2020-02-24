package frc.team568.robot.bart;

import static edu.wpi.first.wpilibj.XboxController.Button.*;

import java.util.Map;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.PanTiltCamera;
import frc.team568.robot.subsystems.TalonSRXDrive;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	public TalonSRXDrive drive;
	public PanTiltCamera camera;
	private Command autonomousCommand;
	XinputController driver = new XinputController(0);

	public Robot() {
		super("Bart");

		config("drive/leftMotors", new Integer[]{1, 2});
		config("drive/rightMotors", new Integer[] {3, 4});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);

		driver.getButton(kBack).whenPressed(drive::toggleIsReversed);
		driver.getButton(kStart).whenPressed(drive::toggleTankControls);
		driver.getButton(kStickLeft)
			.and(driver.getButton(kStickRight))
			.whileActiveOnce(new SequentialCommandGroup(
				new WaitCommand(5),
				new InstantCommand(drive::toggleSafeMode)
			));

		drive = addSubsystem(TalonSRXDrive::new);
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driver.getY(Hand.kLeft),
			Input.TURN, () -> driver.getX(Hand.kRight),
			Input.TANK_LEFT, () -> -driver.getY(Hand.kLeft),
			Input.TANK_RIGHT, () -> -driver.getY(Hand.kRight)
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
