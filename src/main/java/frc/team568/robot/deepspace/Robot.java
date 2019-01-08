package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import frc.team568.robot.RobotBase;

public class Robot extends RobotBase {
	Command autonomousCommand;

	public Robot() {

	}
	
	@Override
	public void robotInit() {

	}

	@Override
	public void robotPeriodic() {
		
	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {

		Scheduler.getInstance().run();

	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			autonomousCommand = null;
		}

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
}
