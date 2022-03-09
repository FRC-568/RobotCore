package frc.team568.robot.rapidreact;

import java.util.Map;
import java.util.Set;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
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

	private ShuffleboardTab tab = Shuffleboard.getTab("Autonomous Parameters");

	private NetworkTableEntry taxiTimeout = tab.add("Taxi Timeout", 1.5).withWidget(BuiltInWidgets.kNumberSlider)
	.withProperties(Map.of("min", 0.5, "max", 4)).getEntry();
	private NetworkTableEntry LidTimeout = tab.add("Lid Timeout", 1).withWidget(BuiltInWidgets.kNumberSlider)
	.withProperties(Map.of("min", 0.5, "max", 5)).getEntry();
	private NetworkTableEntry IntakeTimeout = tab.add("Intake Timeout", 3).withWidget(BuiltInWidgets.kNumberSlider)
	.withProperties(Map.of("min", 0.5, "max", 5)).getEntry();

	Command autonomousCommand;

	String trajectoryJSON = "src/main/deploy/paths/output/Test.wpilib.json";
	Trajectory trajectory = new Trajectory();

	XinputController mainDriver;
	XinputController coDriver;

	Lift lift;
	Intake intake;

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

		taxiTimeout.setPersistent();
		LidTimeout.setPersistent();
		IntakeTimeout.setPersistent();

		var msdefault = new MecanumSubsystemDefaultCommand(drive)
				.useAxis(Input.FORWARD, () -> -mainDriver.getLeftY())
				.useAxis(Input.STRAFE, () -> mainDriver.getLeftX())
				.useAxis(Input.TURN, () -> mainDriver.getRightX());

		drive.setDefaultCommand(msdefault);
	
		mainDriver.getButton(XboxController.Button.kY).whenPressed(msdefault::toggleUseFieldRelative);
		mainDriver.getButton(XboxController.Button.kA).whenPressed(lift::toggle);
		
		coDriver.getButton(XboxController.Button.kX).whenPressed(lift::toggle);
		coDriver.getButton(XboxController.Button.kA).whenPressed(intake::toggleLid);
		coDriver.getButton(XboxController.Button.kB).whenPressed(intake::toggleLift);

		lift.setDefaultCommand(new Command() {
			@Override
			public void execute() {
				lift.setMotor(MathUtil.applyDeadband(mainDriver.getRightTriggerAxis() - mainDriver.getLeftTriggerAxis(), 0.1));
			}

			@Override
			public Set<Subsystem> getRequirements() {
				return Set.of(lift);
			}
		});
		
		intake.setDefaultCommand(new Command() {
			@Override
			public void execute() {
				intake.setIntakeMotor(MathUtil.applyDeadband(coDriver.getRightTriggerAxis() - coDriver.getLeftTriggerAxis(), 0.1));
			}

			@Override
			public Set<Subsystem> getRequirements() {
				return Set.of(intake);
			}
		});

		lift = new Lift(
			Config.Lift.kLiftuprightFlow, 
			Config.Lift.kLiftslantedFlow, 
			Config.Lift.kmotorLift_ID);
		intake = new Intake(
			Config.Intake.kintakeLiftUp, 
			Config.Intake.kintakeLiftDown, 
			Config.Intake.kintakeLidOpen, 
			Config.Intake.kintakeLidClosed, 
			Config.Intake.kmotorIntake_ID);
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		compressor.enableDigital();
		autonomousCommand = new AutoTaxi(drive, intake).setTaxiTime(taxiTimeout.getDouble(1.5)).setLidTime(LidTimeout.getDouble(1)).setIntakeTime(IntakeTimeout.getDouble(3));
		if(autonomousCommand != null)
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
		if (autonomousCommand != null) autonomousCommand.cancel();
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