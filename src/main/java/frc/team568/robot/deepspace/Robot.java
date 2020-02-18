package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.BlinkinLights;
import frc.team568.robot.subsystems.BlinkinLights.Color;
import frc.team568.robot.subsystems.EvoDriveShifter;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	Command autonomousCommand;
	PowerDistributionPanel pdp;
	Compressor compressor;

	TalonSRXDrive drive;
	EvoDriveShifter shifter;
	HabitatClimber climber;
	Relay spikeFront;
	Relay spikeBack;
	Lift lift;
	Claw claw;
	Shpaa shpaa;
	Camera camera;
	BlinkinLights lights;

	// public UsbCamera cameraFront, cameraBack;

	public Robot() {
		super("Deepspace");

		config("drive/leftMotors", new Integer[] { 4, 3 });
		config("drive/rightMotors", new Integer[] { 2, 1 });
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);
		config("shifter/solenoidLow", 0);
		config("shifter/solenoidHigh", 3);

		config("drive/useTankControls", true);

		config("climber/motorDrive", 8);
		config("climber/motorClimbFront", 6);
		config("climber/motorClimbBack", 7);

		config("shpaa/extenderOut", 7);
		config("shpaa/extenderIn", 1);
		config("shpaa/grabberOpen", 2);
		config("shpaa/grabberClose", 6);

		config("claw/solenoidOpen", 4);
		config("claw/solenoidClose", 3);

		config("lift/motorLift", 5);
		config("lift/homeSwitch", 0);

		config("blinkin/control", 8);
		//camera LED ring is on PWM 7

		axis("forward", () -> -axis(0 , Xinput.LeftStickY));
		axis("turn", () -> (Math.abs(axis(0, Xinput.RightStickX)) < 0.15) ? 0 : axis(0, Xinput.RightStickX));
		axis("left", () -> -axis(0 , Xinput.LeftStickY));
		axis("right", () -> -axis(0, Xinput.RightStickY));
		pov("shifterToggle", 1, LEFT);
		button("idleMotors", () -> !button(0, Xinput.RightBumper) && button(0, Xinput.A));
		button("stopMotors", () -> !button(0, Xinput.RightBumper) && button(0, Xinput.B));
		button("driveReverse", 0, Xinput.Back);

		button("launch", 0, Xinput.X);

		axis("climberFront", () -> (Math.abs(axis(1, Xinput.LeftStickY)) < 0.15) ? 0 : axis(1, Xinput.LeftStickY));
		axis("climberBack", () -> axis(1, Xinput.RightTrigger) - axis(1, Xinput.LeftTrigger));
		axis("climberDrive", () -> (button(1, Xinput.RightBumper) ? 1 : 0) + (button(1, Xinput.LeftBumper) ? -1 : 0));

		button("bookmarkButton", 1, Xinput.Back);
		button("tankModeToggle", 0, Xinput.Start);
		button("safeModeToggle", () -> button(0, Xinput.LeftStickIn) && button(0, Xinput.RightStickIn));
		axis("lift", () -> axis(1, Xinput.RightTrigger) - axis(1, Xinput.LeftTrigger));
		
		pov("moveToPosition1", 1, DOWN);
		pov("moveToPosition2", 1, RIGHT);
		pov("moveToPosition3", 1, UP);
		button("liftForCargo", 1, Xinput.Y);

		button("shpaaGrabberToggle", 1, Xinput.A);
		button("shpaaExtenderToggle", 1, Xinput.X);

		button("clawToggle", 1, Xinput.B);

		pdp = new PowerDistributionPanel();
		compressor = new Compressor();
		camera = new Camera();
		spikeFront = new Relay(2);	
		spikeBack = new Relay(3);

		drive = addSubsystem(TalonSRXDrive::new);
		shifter = addSubsystem(EvoDriveShifter::new);
		climber = addSubsystem(HabitatClimber::new);
		lift = addSubsystem(Lift::new);
		//claw = addSubsystem(Claw::new);
		shpaa = addSubsystem(Shpaa::new);

		lights = addSubsystem(BlinkinLights::new);
		camera.initCamera();
		// cameraFront = CameraServer.getInstance().startAutomaticCapture(0);
		// cameraBack = CameraServer.getInstance().startAutomaticCapture(1);
	}

	@Override
	public void robotInit() {
		lights.setColor(Color.RAINBOW_PARTY);
		camera.driveToTapeCommand();
		camera.toggleCameraCommand();
	}

	@Override
	public void robotPeriodic() {
		if(RobotController.getUserButton()) {
			shpaa.setExtenderOut(false);
			shpaa.setGrabberOpen(true);
			shifter.shiftLow();
		}
	}

	@Override
	public void autonomousInit() {
		compressor.setClosedLoopControl(true);
		
		spikeFront.setDirection(Relay.Direction.kForward);
		spikeBack.setDirection(Relay.Direction.kForward);

		lights.setTeamColor();
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
		//camera.processImage();	
	}

	@Override
	public void teleopInit() {
		compressor.setClosedLoopControl(true);
		
		spikeFront.setDirection(Relay.Direction.kForward);
		spikeBack.setDirection(Relay.Direction.kForward);

		lights.setTeamColor();
	}

	@Override
	// Called every 20 milliseconds in teleop
	public void teleopPeriodic() { 
		CommandScheduler.getInstance().run();
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			autonomousCommand = null;
		}

		compressor.setClosedLoopControl(false);
		spikeFront.setDirection(Relay.Direction.kReverse);
		spikeBack.setDirection(Relay.Direction.kReverse);
	}

	@Override
	public void disabledPeriodic() {
		CommandScheduler.getInstance().run();
	}

}
