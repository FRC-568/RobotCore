package org.usfirst.frc.team568.robot.bart;

import org.usfirst.frc.team568.robot.PMW3901;
import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.Locator;
import org.usfirst.frc.team568.util.Vector2;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	public WestCoastDrive driveTrain;
	public PMW3901 flow;
	public Locator locator;
	private Command autonomousCommand;

	public Robot() {
		port("leftFrontMotor", 0);
		port("leftBackMotor", 1);
		port("rightFrontMotor", 2);
		port("rightBackMotor", 3);

		driveTrain = new WestCoastDrive(this, Talon::new);
		locator = new Locator(this);
		addSubsystem(Locator.class, locator);
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		flow.startAutoLoop();
	}

	@Override
	public void teleopPeriodic() {
		locator.update();
		Scheduler.getInstance().run();
		Vector2 postition = flow.getPosition();
		System.out.println("X " + postition.x + " Y " + postition.y);
	}

	@Override
	public void disabledInit() {
		flow.stopAutoLoop();

	}

	@Override
	public void autonomousInit() {
		autonomousCommand = new DriveForward(driveTrain, 5);
		autonomousCommand.start();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

}
