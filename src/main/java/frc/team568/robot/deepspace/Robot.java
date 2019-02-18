package frc.team568.robot.deepspace;

import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.EvoDriveShifter;
import frc.team568.robot.subsystems.TalonSRXDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	Command autonomousCommand;
	private Compressor compressor;
	
	TalonSRXDrive drive;
	EvoDriveShifter shifter;	
	Camera camera;

	public Robot() {
		super("Deepspace");
		
		config("drive/leftMotors", new Integer[]{2, 3});
		config("drive/rightMotors", new Integer[] {4, 5});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);

		config("shifter/solenoidLow", 0);
		config("shifter/solenoidHigh", 3);

		axis("forward", 0, Xinput.LeftStickY);
		axis("turn", 0, Xinput.RightStickX);
		button("shifterToggle", 0, Xinput.Y);
		button("idleMotors", 0, Xinput.A);
		button("stopMotors", 0, Xinput.B);

		compressor = new Compressor();

		drive = addSubsystem(TalonSRXDrive::new);
		shifter = addSubsystem(EvoDriveShifter::new);
		//camera.initCamera();	
	}

	@Override
	public void robotInit() {

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
		
		//camera.processImage();	
	}

	@Override
	public void teleopInit() {
		compressor.setClosedLoopControl(true);
	}

	@Override
	// Called every 20 milliseconds in teleop
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

		compressor.setClosedLoopControl(false);
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
}
