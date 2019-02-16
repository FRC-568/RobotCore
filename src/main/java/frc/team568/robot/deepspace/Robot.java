package frc.team568.robot.deepspace;

import frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	Command autonomousCommand;
	private Compressor compressor;
	
	Drive drive;	
	Camera camera;

	public Robot() {
		super("deepspace");

		compressor = new Compressor();

		drive.initDrive();
		camera.initCamera();	
	}

	@Override
	public void robotInit() {
		
		compressor = new Compressor();
	}

	@Override
	public void robotPeriodic() {

	}

	@Override
	public void autonomousInit() {
		compressor.setClosedLoopControl(true);
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
		camera.processImage();	
	}

	@Override
	public void teleopInit() {
		compressor.setClosedLoopControl(true);
		drive.driveToTapeCommand();
	}

	@Override
	// Called every 20 milliseconds in teleop
	public void teleopPeriodic() { 
		Scheduler.getInstance().run();
		
		drive.driveTank();
		
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

		compressor.setClosedLoopControl(false);
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
}
