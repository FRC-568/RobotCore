package frc.team568.robot.rapidreact;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	PowerDistribution pdp;
	MecanumSubsystem drive;
	Gyro gyro;
	private ShuffleboardTab tab = Shuffleboard.getTab("Drive");
	private NetworkTableEntry maxSpeed = tab.add("Max Speed", 1).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
 
	
	Command autonomousCommand;
	
	String trajectoryJSON = "paths/output/TestPath.wpilib.json";
	Trajectory trajectory = new Trajectory();

	XinputController driverController;

	public Robot() {
		super("RapidReact");
		driverController = new XinputController(0);
		pdp = new PowerDistribution();
		gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
		drive = new MecanumSubsystem(this, gyro);
 
		drive.setDefaultCommand(new MecanumSubsystemDefaultCommand(drive)
		.useAxis(Input.FORWARD, () -> driverController.getLeftY())
		.useAxis(Input.STRAFE, () -> driverController.getLeftX())
		.useAxis(Input.TURN, () -> driverController.getRightX()));
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		try {
			// Opens Trajectory File
			Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
			trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
			// Initializes Autonomous and runs
			autonomousCommand = new Autonomous(trajectory, drive, maxSpeed.getDouble(1.0));
			autonomousCommand.schedule();
		} catch (IOException ex) {
			DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
		}
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) autonomousCommand.cancel();
	}
}