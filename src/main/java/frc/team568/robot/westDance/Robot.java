// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.westDance;

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
		// LiveWindow.disableAllTelemetry();
		//LiveWindow.enableTelemetry(container.drive);
		//LiveWindow.enableTelemetry(container.lift);

		if (m_autonomousCommand != null)
			m_autonomousCommand.cancel();
	}

	@Override
	public void autonomousInit() {
		LiveWindow.disableAllTelemetry();

		//m_autonomousCommand = container.getAutonomousCommand();
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
		LiveWindow.disableAllTelemetry();

		if (m_autonomousCommand != null)
			m_autonomousCommand.cancel();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopPeriodic() {
	}


}
