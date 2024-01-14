package frc.team568.robot.mechy;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.MechyDrive;

import static edu.wpi.first.wpilibj.XboxController.Button.*;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends RobotBase {
	public static final int drivingControllerPort = 0;
	
	final MechyDrive drive;
	Command autonomousCommand;

	XinputController driverController = new XinputController(drivingControllerPort);

	public Robot() {
		super("Mechy");

		drive = new MechyDrive(1, 2, 3, 4)
			.useGyro()
			.buildControlCommand()
				.withForwardAxis(() -> -driverController.getRawAxis(Xinput.LeftStickY))
				.withSideAxis(() -> driverController.getRawAxis(Xinput.LeftStickX))
				.withTurnAxis(() -> driverController.getRawAxis(Xinput.RightStickX))
				.makeDefault();

		driverController.getButton(kStart)
			.onTrue(new InstantCommand(drive::toggleFieldPOV));
		driverController.getButton(kLeftStick)
			.and(driverController.getButton(kRightStick))
			.debounce(5)
			.onTrue(new InstantCommand(drive::toggleSafeMode));
		driverController.getButton(kLeftBumper)
			.onTrue(new InstantCommand(drive::toggleDriftCorrection));

	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		LiveWindow.disableAllTelemetry();
	}

	@Override
	public void autonomousInit() {
		if (autonomousCommand != null)
			autonomousCommand.schedule();
	}

	@Override
	public void testInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		LiveWindow.enableAllTelemetry();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

}
