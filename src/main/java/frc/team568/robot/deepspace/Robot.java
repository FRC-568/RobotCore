package frc.team568.robot.deepspace;

import static edu.wpi.first.wpilibj.XboxController.Button.kA;
import static edu.wpi.first.wpilibj.XboxController.Button.kB;
import static edu.wpi.first.wpilibj.XboxController.Button.kBack;
import static edu.wpi.first.wpilibj.XboxController.Button.kRightBumper;
import static edu.wpi.first.wpilibj.XboxController.Button.kStart;
import static edu.wpi.first.wpilibj.XboxController.Button.kLeftStick;
import static edu.wpi.first.wpilibj.XboxController.Button.kRightStick;
import static edu.wpi.first.wpilibj.XboxController.Button.kX;
import static frc.team568.robot.XinputController.Direction.kLeft;

import java.util.Map;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
	Gyro gyro = new ADXRS450_Gyro();

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

		// redline shifter high <-> low gear
		copilot.getButton(kLeft).whenPressed(shifter::shiftToggle);

		// let motors coast (if configured)
		driver.getButton(kRightBumper)
			.and(driver.getButton(kA))
			.whileActiveContinuous(
				() -> drive.arcadeDrive(0, 0),
				drive);
		
		// stop. now.
		driver.getButton(kRightBumper)
			.and(driver.getButton(kB))
			.whileActiveContinuous(
				drive.drive::stopMotor,
				drive);
		
		// reverse driving direction
		driver.getButton(kBack).whenPressed(drive::toggleIsReversed);

		// launch mode - full speed forward
		driver.getButton(kX).whileActiveContinuous(() -> drive.arcadeDrive(1, 0), drive);

		axis("climberFront", () -> (Math.abs(axis(1, Xinput.LeftStickY)) < 0.15) ? 0 : axis(1, Xinput.LeftStickY));
		axis("climberBack", () -> axis(1, Xinput.RightTrigger) - axis(1, Xinput.LeftTrigger));
		axis("climberDrive", () -> (button(1, Xinput.RightBumper) ? 1 : 0) + (button(1, Xinput.LeftBumper) ? -1 : 0));

		button("bookmarkButton", 1, Xinput.Back);
		driver.getButton(kStart).whenPressed(drive::toggleTankControls);
		driver.getButton(kLeftStick)
			.and(driver.getButton(kRightStick))
			.whileActiveOnce(new SequentialCommandGroup(
				new WaitCommand(5),
				new InstantCommand(drive::toggleSafeMode)
			));
		axis("lift", () -> axis(1, Xinput.RightTrigger) - axis(1, Xinput.LeftTrigger));
		
		pov("moveToPosition1", 1, DOWN);
		pov("moveToPosition2", 1, RIGHT);
		pov("moveToPosition3", 1, UP);
		button("liftForCargo", 1, Xinput.Y);

		button("shpaaGrabberToggle", 1, Xinput.A);
		button("shpaaExtenderToggle", 1, Xinput.X);

		button("clawToggle", 1, Xinput.B);

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

		shifter = new EvoDriveShifter(this);
		climber = new HabitatClimber(this);
		lift = new Lift(this);
		//claw = new Claw(this);
		shpaa = new Shpaa(this);

		lights = new BlinkinLights(this);
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
		compressor.enableDigital();
		
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
		compressor.enableDigital();
		
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

		compressor.disable();;
		spikeFront.setDirection(Relay.Direction.kReverse);
		spikeBack.setDirection(Relay.Direction.kReverse);
	}

	@Override
	public void disabledPeriodic() {
		CommandScheduler.getInstance().run();
	}
}
