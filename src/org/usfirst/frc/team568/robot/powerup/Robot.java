package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	Command autonomousCommand;
	public Drive2018 driveTrain;
	public OI oi;
	protected static Robot instance;

	public Robot() {
		port("leftFrontMotor", 0);
		port("leftBackMotor", 1);
		port("rightFrontMotor", 2);
		port("rightBackMotor", 3);

		instance = this;
		oi = new OI();
		driveTrain = new Drive2018(this);

	}

	@Override
	public void robotInit() {

	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {

		autonomousCommand.start();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
