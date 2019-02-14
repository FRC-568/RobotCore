package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.AnalogGyro;

public class Robot extends RobotBase {
	Command autonomousCommand;

	private Compressor compressor;
	DoubleSolenoid solenoidShifter;
	
	boolean isShifted;
	boolean shiftIsHeld;

	//motor ports
	private static final int lMotorPort = 1;
	private static final int rMotorPort = 2;

	private static final int gyroPort = 0;
	private static final double AngleSetpoint = 0.0;
 	private static final double kP = 0.005; // propotional turning constant

  	// gyro calibration constant, may need to be adjusted;
  	// gyro value of 360 is set to correspond to one full revolution
  	private static final double kVoltsPerDegreePerSecond = 0.0128;

	AnalogGyro gyro;
	public DifferentialDrive drive;
	
	public Joystick controller1;	
	UsbCamera camera;
	GripPipeline gripPipeline;
	CvSink cvSink;
	Mat mat;

	public Timer timer;

	DriveTrain2019 driveTrain;

	// Camera server port
	private final int cameraServerPort = 0;

	public Robot() {
		super("deepspace");
		
		// Compressor
		compressor = new Compressor();

		WPI_TalonSRX leftMotor = new WPI_TalonSRX(lMotorPort);	
		WPI_TalonSRX Rightmotor = new WPI_TalonSRX(rMotorPort);

		drive = new DifferentialDrive(leftMotor, Rightmotor);
		
		leftMotor.setInverted(true);
		Rightmotor.setInverted(true);

		controller1 = new Joystick(0);
		gyro = new AnalogGyro(gyroPort);	
	}

	@Override
	public void robotInit() {
		compressor.start();
		CameraServer.getInstance().startAutomaticCapture(cameraServerPort);
		camera = CameraServer.getInstance().startAutomaticCapture();
		gripPipeline = new GripPipeline();
		cvSink = CameraServer.getInstance().getVideo();
		mat = new Mat();
		
		solenoidShifter = new DoubleSolenoid(0, 1);
		
		compressor = new Compressor();
	}

	@Override
	public void robotPeriodic() {

	}

	@Override
	public void autonomousInit() {
		compressor.setClosedLoopControl(true);
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		//drive.tankDrive(0.5, 0.5);
		cvSink.grabFrame(mat);
		gripPipeline.process(mat);
	}

	@Override
	public void teleopInit() {
		compressor.setClosedLoopControl(true);
		new JoystickButton(controller1, Xinput.X).whileHeld(new TapeTrackerCommand(driveTrain));
	}

	@Override
	// Called every 20 milliseconds in teleop
	public void teleopPeriodic() { 
		Scheduler.getInstance().run();
		
		drive.tankDrive(controller1.getRawAxis(1), controller1.getRawAxis(5));

		if(controller1.getRawButton(Xinput.Y) ) {
			if(!shiftIsHeld) {
				if(isShifted) {
					solenoidShifter.set(DoubleSolenoid.Value.kReverse); //high gear
				} else {			
					solenoidShifter.set(DoubleSolenoid.Value.kForward); //low gear
				}
				isShifted = !isShifted;
			}
			shiftIsHeld = true;
		} else {
			shiftIsHeld = false;
		}		
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
