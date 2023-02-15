// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.chargedup;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
	private RobotContainer container;
	private Command m_autonomousCommand;

	@Override
	public void robotInit() {
		container = new RobotContainer();
	}

	@Override
	public void teleopInit() {
		LiveWindow.disableAllTelemetry();

		if (m_autonomousCommand != null)
			m_autonomousCommand.cancel();
	}

	@Override
	public void autonomousInit() {
		LiveWindow.disableAllTelemetry();

		m_autonomousCommand = container.getAutonomousCommand();
		if (m_autonomousCommand != null)
			m_autonomousCommand.schedule();
	}

	@Override
	public void testInit() {
		LiveWindow.enableAllTelemetry();

		if (m_autonomousCommand != null)
			m_autonomousCommand.cancel();
	}

	@Override
	public void disabledInit() {
		// Enable telemetry for testing - disable before competition
		LiveWindow.enableAllTelemetry();

		if (m_autonomousCommand != null)
			m_autonomousCommand.cancel();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		driveWithJoystick(false);
	}

	@Override
	public void autonomousPeriodic() {
		driveWithJoystick(false);
		container.drive.updateOdometry();
	}

	@Override
	public void teleopPeriodic() {
		driveWithJoystick(true);
	}

	// TODO: move this into a default command for SwerveSubsystem.
	private void driveWithJoystick(boolean fieldRelative) {
		// Get the x speed. We are inverting this because Xbox controllers return
		// negative values when we push forward.
		final var xSpeed = OI.Axis.swerveForward.getAsDouble();

		// Get the y speed or sideways/strafe speed. We are inverting this because
		// we want a positive value when we pull to the left. Xbox controllers
		// return positive values when you pull to the right by default.
		final var ySpeed = OI.Axis.swerveLeft.getAsDouble();

		// Get the rate of angular rotation. We are inverting this because we want a
		// positive value when we pull to the left (remember, CCW is positive in
		// mathematics). Xbox controllers return positive values when you pull to
		// the right by default.
		final var rot = OI.Axis.swerveCCW.getAsDouble();

		container.drive.drive(xSpeed, ySpeed, rot, fieldRelative);
	}
}
