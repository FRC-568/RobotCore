// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.crescendo;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import swervelib.telemetry.SwerveDriveTelemetry;

public class Robot extends TimedRobot {
	private RobotContainer container;
	private Command m_autonomousCommand;

	private final I2C.Port i2cPort = I2C.Port.kOnboard;
	private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
	private final ColorMatch m_colorMatcher = new ColorMatch();

	//add color, i dont know values
	private final Color kTarget = new Color();

	@Override
	public void robotInit() {
		container = new RobotContainer();
		if (DriverStation.isFMSAttached())
			Shuffleboard.selectTab(OI.autoTab.getTitle());
		else
			SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
		
		m_colorMatcher.addColorMatch(kTarget);
	}

	@Override
	public void teleopInit() {
		// LiveWindow.disableAllTelemetry();
		//LiveWindow.enableTelemetry(container.drive);
		//LiveWindow.enableTelemetry(container.lift);

		if (m_autonomousCommand != null)
			m_autonomousCommand.cancel();
			
		if (DriverStation.isFMSAttached())
			Shuffleboard.selectTab(OI.driverTab.getTitle());
	}

	@Override
	public void autonomousInit() {
		LiveWindow.disableAllTelemetry();

		m_autonomousCommand = container.getAutonomousCommand();
		if (m_autonomousCommand != null)
			m_autonomousCommand.schedule();

		if (DriverStation.isFMSAttached())
			Shuffleboard.selectTab(OI.driverTab.getTitle());
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

		Color detectedColor = m_colorSensor.getColor();
		String colorString;
    	ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
		if (match.color == kTarget) {
			colorString = "Note";
		}else {
			colorString = "Unknown";
		}

		SmartDashboard.putString("Detected Color", colorString);
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
