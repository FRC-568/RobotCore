package frc.team568.robot.rapidreact;

import java.util.Set;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	PowerDistribution pdp;
	MecanumSubsystem drive;
	Gyro gyro;
	Compressor compressor;
	UsbCamera camera;
	PneumaticsControlModule pcm;
	Lift lift;
	Intake intake;

	BuiltInAccelerometer accelerometer;

	private SendableChooser<Command> m_chooser;

	Command autonomousCommand;
	public AutonomousParameters autoParam;

	String trajectoryJSON = "src/main/deploy/paths/output/Test.wpilib.json";
	Trajectory trajectory = new Trajectory();

	XinputController mainDriver;
	XinputController coDriver;

	public Robot() {
		super("RapidReact");
		mainDriver = new XinputController(0);
		coDriver = new XinputController(1);
		pdp = new PowerDistribution();
		pcm = new PneumaticsControlModule();
		gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
		drive = new MecanumSubsystem(gyro);
		compressor = new Compressor(Config.kcompressor, PneumaticsModuleType.CTREPCM);
		camera = CameraServer.startAutomaticCapture();
		accelerometer = new BuiltInAccelerometer();
		autoParam = new AutonomousParameters();

		lift = new Lift();
		lift.setDefaultCommand(new Command() {
			@Override
			public void execute() {
				lift.setMotor(MathUtil.applyDeadband(mainDriver.getRightTriggerAxis() - mainDriver.getLeftTriggerAxis(),
						0.1));
			}

			@Override
			public Set<Subsystem> getRequirements() {
				return Set.of(lift);
			}
		});

		intake = new Intake();
		intake.setDefaultCommand(new Command() {
			@Override
			public void execute() {
				intake.setIntakeMotor(
						MathUtil.applyDeadband(coDriver.getLeftTriggerAxis(), 0.05) - MathUtil.applyDeadband(coDriver.getRightTriggerAxis(), 0.05));
			}

			@Override
			public Set<Subsystem> getRequirements() {
				return Set.of(intake);
			}
		});

		var msdefault = new MecanumSubsystemDefaultCommand(drive)
				.useAxis(Input.FORWARD, () -> -mainDriver.getLeftY())
				.useAxis(Input.STRAFE, () -> mainDriver.getLeftX())
				.useAxis(Input.TURN, () -> mainDriver.getRightX());

		drive.setDefaultCommand(msdefault);

		mainDriver.getButton(XboxController.Button.kY).whenPressed(msdefault::toggleUseFieldRelative);
		mainDriver.getButton(XboxController.Button.kX).whenPressed(lift::toggle);
		mainDriver.getButton(XboxController.Button.kA).whenPressed(() -> msdefault.ram(accelerometer, intake));

		coDriver.getButton(XboxController.Button.kX).whenPressed(lift::toggle);
		coDriver.getButton(XboxController.Button.kA).whenPressed(intake::toggleLid);
		coDriver.getButton(XboxController.Button.kB).whenPressed(intake::toggleLift);
		coDriver.getButton(XboxController.Button.kY).whenPressed(this::toggleCompressor);

		new Button(RobotController::getUserButton).whenReleased(this::reset);
	}

	public void reset(){
		if(!intake.isLiftUp()) intake.setLiftUp(true);
		if(!lift.isUpright()) lift.setUpright(false);
		if(!intake.isLidOpen()) intake.setLidOpen(true);
	}

	private void toggleCompressor() {
		if(compressor.enabled()) compressor.disable();
		else compressor.enableDigital();
	}

	public void robotInit() {
		m_chooser = new SendableChooser<>();
		m_chooser.setDefaultOption("Outake and Taxi", new Autonomous("Outtake", drive, intake, autoParam));

		m_chooser.addOption("Only Taxi", new Autonomous("Taxi", drive, intake, autoParam));

		m_chooser.addOption("Do Nothing", new Autonomous("Do Nothing", drive, intake, autoParam));
		
		autoParam.tab.add(m_chooser);
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		compressor.enableDigital();
		autonomousCommand = m_chooser.getSelected();
		if (autonomousCommand != null)
			autonomousCommand.schedule();
		/*
		 * try {
		 * // Opens Trajectory File
		 * Path trajectoryPath =
		 * Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
		 * trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
		 * // Initializes Autonomous and schedules it
		 * autonomousCommand = new Autonomous(trajectory, drive,
		 * maxSpeed.getDouble(1.0));
		 * autonomousCommand.schedule();
		 * } catch (IOException ex) {
		 * DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON,
		 * ex.getStackTrace());
		 * }
		 */
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		compressor.enableDigital();
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			autonomousCommand = null;
		}
		compressor.disable();
	}
}