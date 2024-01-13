package frc.team568.robot.deepspace;

import static edu.wpi.first.wpilibj.XboxController.Button.*;
import static frc.team568.robot.XinputController.Direction.*;

import java.util.Map;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.BlinkinLights;
import frc.team568.robot.subsystems.BlinkinLights.Color;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.EvoDriveShifter;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	Command autonomousCommand;
	PowerDistribution pdp;
	Compressor compressor;
	Gyro gyro = (Gyro) new ADXRS450_Gyro();

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

	XinputController driver = new XinputController(0);
	XinputController copilot = new XinputController(1);

	// public UsbCamera cameraFront, cameraBack;

	public Robot() {
		super("Deepspace");

		config("drive/leftMotors", new Integer[] { 4, 3 });
		config("drive/rightMotors", new Integer[] { 2, 1 });
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);
		config("drive/useTankControls", true);

		//camera LED ring is on PWM 7

		// let motors coast (if configured)
		driver.getButton(kRightBumper)
			.and(driver.getButton(kA))
			.whileTrue(new RunCommand(
				() -> drive.arcadeDrive(0, 0),
				drive));
		
		// stop. now.
		driver.getButton(kRightBumper)
			.and(driver.getButton(kB))
			.whileTrue(new RunCommand(
				drive.drive::stopMotor,
				drive));
		
		// reverse driving direction
		driver.getButton(kBack).onTrue(new InstantCommand(drive::toggleIsReversed));

		// launch mode - full speed forward
		driver.getButton(kX).whileTrue(new RunCommand(() -> drive.arcadeDrive(1, 0), drive));

		driver.getButton(kStart).onTrue(new InstantCommand(drive::toggleTankControls));

		driver.getButton(kLeftStick)
			.and(driver.getButton(kRightStick))
			.debounce(5)
			.onTrue(new InstantCommand(drive::toggleSafeMode));

		pdp = new PowerDistribution();
		compressor = new Compressor(PneumaticsModuleType.CTREPCM);
		camera = new Camera();
		spikeFront = new Relay(2);	
		spikeBack = new Relay(3);

		drive = new TalonSRXDrive(this).withGyro(gyro);
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driver.getLeftY(),
			Input.TURN, () -> Math.abs(driver.getRightX()) < 0.15 ? 0 : driver.getRightX() * 0.7,
			Input.TANK_LEFT, () -> -driver.getLeftY(),
			Input.TANK_RIGHT, () -> -driver.getRightY()
		)));

		shifter = new EvoDriveShifter(
			Config.Shifter.kLowSolenoidPort,
			Config.Shifter.kHighSolenoidPort
		);

		climber = new HabitatClimber(
			Config.HabitatClimber.kDrivePort,
			Config.HabitatClimber.kFrontPort,
			Config.HabitatClimber.kBackPort
		).buildControlCommand()
			.withFront(() -> (Math.abs(copilot.getLeftY()) < 0.15) ? 0 : copilot.getLeftY())
			.withBack(() -> copilot.getRightTriggerAxis() - copilot.getLeftTriggerAxis())
			.withDrive(() -> (copilot.getRightBumper() ? 1 : 0) + (copilot.getLeftBumper() ? -1 : 0))
			.makeDefault();

		lift = new Lift(
			Config.Lift.kMotorPort,
			Config.Lift.kHomeSwitchPort
		).withControl(() -> axis(1, Xinput.RightTrigger) - axis(1, Xinput.LeftTrigger));

		Trigger bookmarkReleased = new Trigger(() -> !copilot.getBackButton());

		copilot.getButton(kDown).and(bookmarkReleased).whileTrue(lift.new MoveToCommand(
			() -> Preferences.getDouble("deepspace/lift/" + (copilot.getYButton() ? "cargo" : "hatch") + "1", lift.getPosition())));
		copilot.getButton(kRight).and(bookmarkReleased).whileTrue(lift.new MoveToCommand(
			() -> Preferences.getDouble("deepspace/lift/" + (copilot.getYButton() ? "cargo" : "hatch") + "2", lift.getPosition())));
		copilot.getButton(kUp).and(bookmarkReleased).whileTrue(lift.new MoveToCommand(
			() -> Preferences.getDouble("deepspace/lift/" + (copilot.getYButton() ? "cargo" : "hatch") + "3", lift.getPosition())));

		// Update bookmarks when back button is pressed
		copilot.getButton(kBack).whileTrue(new RunCommand(() -> {
			int level = copilot.getUpButton() ? 3
				: copilot.getRightButton() ? 2
				: copilot.getDownButton() ? 1
				: 0;
			if (level > 0)
				Preferences.setDouble("deepspace/lift/" + (copilot.getYButton() ? "cargo" : "hatch") + level, lift.getPosition());
		}));

		claw = new Claw(
			Config.Claw.kOpenPort,
			Config.Claw.kClosePort
		);

		shpaa = new Shpaa(
			Config.Shpaa.kExtenderOutPort,
			Config.Shpaa.kExtenderInPort,
			Config.Shpaa.kGrabberOpenPort,
			Config.Shpaa.kGrabberClosePort
		);

		lights = new BlinkinLights(Config.BlinkinLights.kControlPort);
		camera.initCamera();
		// cameraFront = CameraServer.startAutomaticCapture(0);
		// cameraBack = CameraServer.startAutomaticCapture(1);
		
		copilot.getButton(kA).onTrue(new InstantCommand(shpaa::toggleGrabber));
		copilot.getButton(kX).onTrue(new InstantCommand(shpaa::toggleExtender));
		copilot.getButton(kB).onTrue(new InstantCommand(claw::toggleOpen));
		copilot.getButton(kLeft).onTrue(new InstantCommand(shifter::shiftToggle)); // redline shifter high <-> low gear

		new Trigger(RobotController::getUserButton).onFalse(new InstantCommand(() -> {
			shpaa.setExtenderOut(false);
			shpaa.setGrabberOpen(true);
			shifter.shiftLow();
		}, shpaa, shifter));
	}

	@Override
	public void robotInit() {
		lights.setColor(Color.RAINBOW_PARTY);
		camera.driveToTapeCommand();
		camera.toggleCameraCommand();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		compressor.enableDigital();
		
		spikeFront.setDirection(Relay.Direction.kForward);
		spikeBack.setDirection(Relay.Direction.kForward);

		lights.setTeamColor();
	}

	@Override
	public void autonomousPeriodic() {
		//camera.processImage();	
	}

	@Override
	public void teleopInit() {
		compressor.enableDigital();
		
		spikeFront.setDirection(Relay.Direction.kForward);
		spikeBack.setDirection(Relay.Direction.kForward);

		lights.setTeamColor();
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			autonomousCommand = null;
		}

		compressor.disable();;
		spikeFront.setDirection(Relay.Direction.kReverse);
		spikeBack.setDirection(Relay.Direction.kReverse);
	}

}
