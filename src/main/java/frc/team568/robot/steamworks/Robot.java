package frc.team568.robot.steamworks;

//import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.grip.VisionProcessor;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.DriveTrain;
import frc.team568.robot.subsystems.VisionTargetTracker;
import frc.team568.robot.subsystems.WinchClimber;

public class Robot extends RobotBase {
	int session;
	Timer time;

	public double speed;
	public double pressure;
	Command autonomousCommand;
	protected static Robot instance;
	public OI oi;
	public DriveTrain driveTrain;
	public ReferenceFrame2017 referenceFrame;
	//public ADIS16448_IMU imu;
	public VisionTargetTracker gearLiftTracker;
	public GearBox gearBox;
	public RopeCollector ropeCollector;
	public WinchClimber winchClimber;
	public Shooter2017 shooter;
	public Compressor compressor;
	public VisionTargetTracker gearTracker;
	public ControllerButtons buttons;
	public VisionProcessor visionProcessor;
	public BlockIntake blockIntake;

	public Robot() {
		super("steamworks");

		instance = this;

		port("leftFrontMotor", 1);
		port("leftBackMotor", 2);
		port("rightFrontMotor", 3);
		port("rightBackMotor", 4);

		port("joy1Pos", 0);
		port("joy2Pos", 1);

		port("gearPneumatic1", 0);
		port("gearPneumatic2", 1);

		port("encoderYellow", 0);
		port("encoderWhite", 1);

		port("intakeOne", 6);
		port("intakeTwo", 7);

		oi = new OI();
		// referenceFrame = new ReferenceFrame2017(this);
		driveTrain = new DriveTrain(this, referenceFrame);
		time = new Timer();
		// imu = new ADIS16448_IMU();
		// compressor = new Compressor();

		// blockIntake = new BlockIntake(this);

		oi.openRopeClamp.whenPressed(ropeCollector.openCommand());
		oi.closeRopeClamp.whenPressed(ropeCollector.closeCommand());
	}

	@Override
	public void robotInit() {
		// imu.reset();
		// imu.calibrate();
		// referenceFrame.reset();
		// referenceFrame.calabrateGyro();

		// CameraServer.getInstance().startAutomaticCapture(0);

		SmartDashboard.putNumber("Autonomous #", 1);
		SmartDashboard.putNumber("Speed Multiplier", 1);

		// oi.intake.whileHeld(blockIntake.getCommandBlockIn());
		// oi.outtake.whileHeld(blockIntake.getCommandBlockOut());

		// compressor.start();

		// SmartDashboard.putNumber("Kp", .15);
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		// referenceFrame.motorEncoder.reset();
		// imu.reset();

		if (SmartDashboard.getNumber("Autonomous #", 0) == 1) {
			autonomousCommand = new AutoOne(this);
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 2) {
			autonomousCommand = new AutoTwo(this);
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 3) {
			autonomousCommand = new AutoThree(this);
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 4) {
			autonomousCommand = new AutoFour(this);
		}
		// referenceFrame.reset();
		// visionProcessor.start();
		autonomousCommand.schedule();
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
		SmartDashboard.putNumber("MotorEncoderTicks:", referenceFrame.motorEncoder.get());
		SmartDashboard.putNumber("Frames", visionProcessor.processingTime);
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

		// referenceFrame.motorEncoder.reset();
		// imu.reset();
		// referenceFrame.reset();
	}

	@Override
	public void teleopPeriodic() {
		CommandScheduler.getInstance().run();

		// SmartDashboard.putNumber("MotorEncoderTicks:",
		// referenceFrame.motorEncoder.get());
		// SmartDashboard.putNumber("GYRO", referenceFrame.getAngle());

	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
