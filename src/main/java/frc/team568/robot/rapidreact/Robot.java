package frc.team568.robot.rapidreact;

import static edu.wpi.first.math.MathUtil.applyDeadband;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
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
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	PowerDistribution pdp;
	MecanumSubsystem drive;
	ADXRS450_Gyro gyro;
	Compressor compressor;
	UsbCamera camera;
	PneumaticsControlModule pcm;
	Lift lift;
	Intake intake;
	BuiltInAccelerometer accel;

	private AutonomousParameters autoParam;
	private SendableChooser<Command> m_chooser;
	Command autonomousCommand;

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
		autoParam = new AutonomousParameters();
		accel = new BuiltInAccelerometer();

		lift = new Lift();
		lift.setDefaultCommand(new RunCommand(
			() -> lift.setMotor(
				applyDeadband(mainDriver.getRightTriggerAxis() - mainDriver.getLeftTriggerAxis(),
				0.1)),
			lift));

		intake = new Intake();
		intake.setDefaultCommand(new RunCommand(
			() -> intake.setIntakeMotor(
				applyDeadband(coDriver.getLeftTriggerAxis(), 0.05) - applyDeadband(coDriver.getRightTriggerAxis(),
				0.05)),
			intake));

		var msdefault = new MecanumSubsystemDefaultCommand(drive)
				.useAxis(Input.FORWARD, () -> -mainDriver.getLeftY())
				.useAxis(Input.STRAFE, () -> mainDriver.getLeftX())
				.useAxis(Input.TURN, () -> mainDriver.getRightX());

		drive.setDefaultCommand(msdefault);

		mainDriver.getButton(XboxController.Button.kY).onTrue(new InstantCommand(msdefault::toggleUseFieldRelative));
		mainDriver.getButton(XboxController.Button.kX).onTrue(new InstantCommand(lift::toggle));
		mainDriver.getButton(XboxController.Button.kA).whileTrue(new ChargeAndScore(drive, intake, 3.0));

		coDriver.getButton(XboxController.Button.kX).onTrue(new InstantCommand(lift::toggle));
		coDriver.getButton(XboxController.Button.kA).onTrue(new InstantCommand(intake::toggleLid));
		coDriver.getButton(XboxController.Button.kB).onTrue(new InstantCommand(intake::toggleLift));
		coDriver.getButton(XboxController.Button.kY).onTrue(new InstantCommand(this::toggleCompressor));
		// coDriver.getButton(XboxController.Button.kY)
		

		new Trigger(RobotController::getUserButton).onTrue(new InstantCommand(this::reset));
	}

	public void reset(){
		intake.setLiftUp(true);
		lift.setUpright(false);
		intake.setLidOpen(true);
	}

	private void toggleCompressor() {
		if(compressor.isEnabled()) compressor.disable();
		else compressor.enableDigital();
	}

	public void robotInit() {
		m_chooser = new SendableChooser<>();
		m_chooser.setDefaultOption("Outake and Taxi", new AutoScoreAndTaxi(drive, intake, autoParam, accel));

		m_chooser.addOption("Only Taxi", new AutoTaxi(drive, autoParam.taxiTime()));

		var wait = new WaitCommand(100);
		wait.addRequirements(drive, intake);
		m_chooser.addOption("Do Nothing", wait);
		
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
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		compressor.enableDigital();
	}

	@Override
	public void testInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		compressor.enableDigital();
		LiveWindow.enableAllTelemetry();
	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		compressor.disable();
		LiveWindow.disableAllTelemetry();
	}
}