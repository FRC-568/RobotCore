package frc.team568.robot.deepspace;

import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.EvoDriveShifter;
import frc.team568.robot.subsystems.TalonSRXDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	Command autonomousCommand;
	private PowerDistributionPanel pdp;
	private Compressor compressor;
	
	TalonSRXDrive drive;
	EvoDriveShifter shifter;
	HabitatClimber climber;
	Lift lift;
	Claw claw;
	Shpaa shpaa;
	Camera camera;

	public Robot() {
		super("Deepspace");
		
		config("drive/leftMotors", new Integer[]{4, 3});
		config("drive/rightMotors", new Integer[] {2, 1});
		config("drive/leftInverted", false);
		config("drive/rightInverted", false);

		config("shifter/solenoidLow", 0);
		config("shifter/solenoidHigh", 3);

		config("climber/motorDrive", 8);
		config("climber/motorClimbFront", 6);
		config("climber/motorClimbBack", 7);

		config("shpaa/extenderOut", 7);
		config("shpaa/extenderIn", 1);
		config("shpaa/grabberOpen", 2);
		config("shpaa/grabberClose", 6);
		
		config("claw/solenoidOpen", 4);
		config("claw/solenoidClose", 5);

		config("lift/motorLift", 5);

		axis("forward", () -> button(0, Xinput.LeftBumper) ? 0 : axis(0, Xinput.LeftStickY));
		axis("turn", () -> button(0, Xinput.LeftBumper) ? 0 : axis(0, Xinput.RightStickX));
		button("shifterToggle", 0, Xinput.Y);
		button("idleMotors", 0, Xinput.A);
		button("stopMotors", 0, Xinput.B);
		button("driveReverse", 0, Xinput.Back);

		axis("climberFront", () -> button(0, Xinput.LeftBumper) ? axis(0, Xinput.LeftStickY) : 0);
		axis("climberBack", () -> button(0, Xinput.LeftBumper) ? axis(0, Xinput.RightStickY) : 0);
		axis("climberDrive", () -> button(0, Xinput.LeftBumper) ? axis(0, Xinput.RightTrigger) - axis(0, Xinput.LeftTrigger) : 0);

		axis("lift", () -> button(0, Xinput.LeftBumper) ? 0 : axis(0, Xinput.RightTrigger) - axis(0, Xinput.LeftTrigger));

		button("shpaaGrabberToggle", () -> button(0, Xinput.RightBumper) && button(0, Xinput.B));
		button("shpaaExtenderToggle", () -> button(0, Xinput.RightBumper) && button(0, Xinput.X));
		
		button("clawToggle", () -> button(0, Xinput.RightBumper) && button(0, Xinput.A));

		pdp = new PowerDistributionPanel();
		compressor = new Compressor();


		drive = addSubsystem(TalonSRXDrive::new);
		shifter = addSubsystem(EvoDriveShifter::new);
		climber = addSubsystem(HabitatClimber::new);
		lift = addSubsystem(Lift::new);
		claw = addSubsystem(Claw::new);
		shpaa = addSubsystem(Shpaa::new);
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
