package frc.team568.robot.deepspace;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
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
	Lift lift;
	Claw claw;
	Shpaa shpaa;
	Camera camera;
	BlinkinLights lights;

	public UsbCamera cameraFront, cameraBack;

	private boolean habMode = false;
	private JoystickButton habModeButton;

	public Robot() {
		super("Deepspace");

		config("drive/leftMotors", new Integer[] { 4, 3 });
		config("drive/rightMotors", new Integer[] { 2, 1 });
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
		config("lift/homeSwitch", 0);

		config("blinkin/control", 8);
		//camera LED ring is on PWM 7

		axis("forward", () -> button(0, Xinput.LeftBumper) ? 0 : -axis(0, Xinput.LeftStickY));
		axis("turn", () -> button(0, Xinput.LeftBumper) || (Math.abs(axis(0, Xinput.RightStickX)) < 0.15) ? 0
				: axis(0, Xinput.RightStickX));
		//button("shifterToggle", 0, Xinput.Y);
		button("idleMotors", () -> !button(0, Xinput.RightBumper) && button(0, Xinput.A));
		button("stopMotors", () -> !button(0, Xinput.RightBumper) && button(0, Xinput.B));
		button("driveReverse", 0, Xinput.Back);

		axis("climberFront", 1, Xinput.RightStickY);
		axis("climberBack", 1, Xinput.LeftStickY);
		axis("climberDrive", () -> axis(1, Xinput.RightTrigger) - axis(1, Xinput.LeftTrigger));

		axis("lift", () -> (button(1, Xinput.RightBumper) ? 1 : 0) + (button(1, Xinput.LeftBumper) ? -1 : 0));

		button("shpaaGrabberToggle", 1, Xinput.B);
		button("shpaaExtenderToggle", 1, Xinput.X);

		button("clawToggle", 1, Xinput.A);

		/*if (true || DriverStation.getInstance().getJoystickIsXbox(0)) { // Joystick Inputs for Xbox controller
 
			axis("forward", () -> button(0, Xinput.LeftBumper) ? 0 : -axis(0, Xinput.LeftStickY));
			axis("turn", () -> button(0, Xinput.LeftBumper) || (Math.abs(axis(0, Xinput.RightStickX)) < 0.15) ? 0
					: axis(0, Xinput.RightStickX));
			button("shifterToggle", 0, Xinput.Y);
			button("idleMotors", () -> !button(0, Xinput.RightBumper) && button(0, Xinput.A));
			button("stopMotors", () -> !button(0, Xinput.RightBumper) && button(0, Xinput.B));
			button("driveReverse", 0, Xinput.Back);

			axis("climberFront", () -> button(0, Xinput.LeftBumper) ? axis(0, Xinput.LeftStickY) : 0);
			axis("climberBack", () -> button(0, Xinput.LeftBumper) ? axis(0, Xinput.RightStickY) : 0);
			axis("climberDrive",
					() -> button(0, Xinput.LeftBumper) ? axis(0, Xinput.RightTrigger) - axis(0, Xinput.LeftTrigger)
							: 0);

			axis("lift", () -> button(0, Xinput.LeftBumper) ? 0
					: axis(0, Xinput.RightTrigger) - axis(0, Xinput.LeftTrigger));

			button("shpaaGrabberToggle", () -> button(0, Xinput.RightBumper) && button(0, Xinput.B));
			button("shpaaExtenderToggle", () -> button(0, Xinput.RightBumper) && button(0, Xinput.X));

			button("clawToggle", () -> button(0, Xinput.RightBumper) && button(0, Xinput.A));

		} else { // Joystick inputs for driverstation

			axis("forward", () -> thrusterAxis(axis(0, 0), button(1, 1)));
			axis("turn", () -> axis(1, 0));
			button("driveReverse", 0, 3);

			axis("climberFront",
			() -> habMode
					? ((button(0, DriverStationInput.RightJoystickUp) ? 1 : 0)
							+ (button(0, DriverStationInput.RightJoystickDown) ? -1 : 0))
					: 0);
			axis("climberBack",
			() -> habMode
					? ((button(0, DriverStationInput.RightJoystickRight) ? 1 : 0)
							+ (button(0, DriverStationInput.RightJoystickLeft) ? -1 : 0))
					: 0);
			axis("climberDrive",
			() -> (button(0, DriverStationInput.ButtonEight) ? 1 : 0)
							+ (button(0, DriverStationInput.ButtonFour) ? -1 : 0)
					);
			axis("lift", () -> (button(0, DriverStationInput.RightJoystickUp) ? 1 : 0)
					+ (button(0, DriverStationInput.RightJoystickDown) ? -1 : 0));
			
			
			button("shpaaExtenderIn", () -> !habMode && button(0, DriverStationInput.RightJoystickLeft));
			button("shpaaExtenderOut", () -> !habMode && button(0, DriverStationInput.RightJoystickRight));

			button("clawClose", 0, DriverStationInput.ButtonTwo);
			button("clawOpen", 0, DriverStationInput.ButtonSix);
			button("shpaaGrabberClose", 0, DriverStationInput.ButtonThree);
			button("shpaaGrabberOpen", 0, DriverStationInput.ButtonSeven);

			button("liftTo1", 0, DriverStationInput.ButtonEleven);
			button("liftTo2", 0, DriverStationInput.ButtonTen);
			button("liftTo3,", 0, DriverStationInput.ButtonNine);
			button("liftForCargo", 0, DriverStationInput.ButtonFive);

		}

		habModeButton = new JoystickButton(new Joystick(0), DriverStationInput.ButtonOne);
		habModeButton.whenPressed(new Command() {

			@Override
			protected void initialize() {
				habMode = !habMode;
			}

			@Override
			protected boolean isFinished() {
				return true;
			}

		});*/

		pdp = new PowerDistributionPanel();
		compressor = new Compressor();

		drive = addSubsystem(TalonSRXDrive::new);
		shifter = addSubsystem(EvoDriveShifter::new);
		climber = addSubsystem(HabitatClimber::new);
		lift = addSubsystem(Lift::new);
		claw = addSubsystem(Claw::new);
		shpaa = addSubsystem(Shpaa::new);

		lights = addSubsystem(BlinkinLights::new);
		//camera.initCamera();
		cameraFront = CameraServer.getInstance().startAutomaticCapture(0);
		cameraBack = CameraServer.getInstance().startAutomaticCapture(1);
	}

	private boolean triggerWasActive = false;
	private double thrusterNeutral = 0;
	private double thrusterAxis(double axis, boolean trigger) {
		if (trigger) {
			if (!triggerWasActive) {
				triggerWasActive = true;
				thrusterNeutral = axis;
			}
			return -(axis - thrusterNeutral);
		} else {
			triggerWasActive = false;
			return 0;
		}
	}

	@Override
	public void robotInit() {
		lights.setColor(Color.RAINBOW_PARTY);
	}

	@Override
	public void robotPeriodic() {

	}

	@Override
	public void autonomousInit() {
		compressor.setClosedLoopControl(true);

		lights.setTeamColor();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
		//camera.processImage();	
	}

	@Override
	public void teleopInit() {
		compressor.setClosedLoopControl(true);
		lights.setTeamColor();
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
