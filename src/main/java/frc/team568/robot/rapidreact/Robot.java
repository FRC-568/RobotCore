package frc.team568.robot.rapidreact;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;

import java.io.IOException;
import java.nio.file.Path;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	SendableChooser<Command> m_chooser;
	PowerDistribution pdp;
	MecanumSubsystem drive;
	Gyro gyro;
	Compressor compressor;

	DoubleSolenoid collectorLift;
	Solenoid climber;
	Solenoid collector;
	// 2 Singles(Intake Closing thing, Main Lift) 1 Double (Intake Lift)

	private ShuffleboardTab tab = Shuffleboard.getTab("Drive");
	private NetworkTableEntry maxSpeed = tab.add("Max Speed", 1).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
	CANSparkMax intakeMotor;
	CANSparkMax liftMotor;

	Command autonomousCommand;

	String trajectoryJSON = "src/main/deploy/paths/output/Test.wpilib.json";
	Trajectory trajectory = new Trajectory();

	XinputController driverController;

	public Robot() {
		super("RapidReact");
		driverController = new XinputController(0);
		pdp = new PowerDistribution();
		gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
		drive = new MecanumSubsystem(gyro);
		compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

		collectorLift = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1); // random values for now
		collector = new Solenoid(PneumaticsModuleType.CTREPCM, 2);
		climber = new Solenoid(PneumaticsModuleType.CTREPCM, 3);

		// changed from brushless to brushed *test*
		intakeMotor = new CANSparkMax(5, MotorType.kBrushed);
		liftMotor = new CANSparkMax(6, MotorType.kBrushed);

		var msdefault = new MecanumSubsystemDefaultCommand(drive)
				.useAxis(Input.FORWARD, () -> -driverController.getLeftY())
				.useAxis(Input.STRAFE, () -> driverController.getLeftX())
				.useAxis(Input.TURN, () -> driverController.getRightX());
		drive.setDefaultCommand(msdefault);
		driverController.getButton(XboxController.Button.kY).whenPressed(msdefault::toggleUseFieldRelative);
		new Button(() -> driverController.getLeftBumper() && driverController.getRightBumper())
				.whenPressed(gyro::reset);
	}

	public void robotInit() {
		m_chooser = new SendableChooser<>();
		m_chooser.setDefaultOption("taxi 1 second", new AutoTaxi(drive));
		// m_chooser.addOption(name, object);
		// Put the chooser on the dashboard
		SmartDashboard.putData(m_chooser);
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		compressor.enableDigital();
		
		autonomousCommand = m_chooser.getSelected();
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
		collectorLift.set(kForward);
	}

	@Override
	public void teleopPeriodic() {
		if (driverController.getAButton()) {
			intakeMotor.set(1);
		}
		// if (driverController.getBButton()){
		// testSolenoid.toggle();
		// }
		driverController.getButton(XboxController.Button.kB).whenPressed(collector::toggle);
		driverController.getButton(XboxController.Button.kA).whenPressed(collectorLift::toggle);
		driverController.getButton(XboxController.Button.kX).whenPressed(climber::toggle);
		liftMotor.set(driverController.getRightY());
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