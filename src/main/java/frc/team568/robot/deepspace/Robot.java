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
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team568.robot.RobotBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends RobotBase {
	Command autonomousCommand;

	private Compressor compressor;

	//motor ports
	private static final int lfMotorPort = 1;
	//private static final int lbMotorPort = 2;
  	//private static final int rfMotorPort = 3;
	private static final int rbMotorPort = 2;

	// //joystick inputs
	//private static final int JoystickInput = 1;
	//private static final int rightJoystickInput = 1;

	public DifferentialDrive drive;
	
	public Joystick controller1;
	public Joystick controller2;
	
	boolean isShifted;
	boolean shiftIsHeld;

	DoubleSolenoid solenoidShifter;
	
	UsbCamera camera;
	GripPipeline gripPipeline;
	CvSink cvSink;
	Mat mat;

	public Timer timer;

	DriveTrain2019 driveTrain;

	// Camera server port
	private final int cameraServerPort = 0;

	public Robot() {
		// Compressor
		compressor = new Compressor();
		WPI_TalonSRX frontLeft = new WPI_TalonSRX(lfMotorPort);	
		//WPI_TalonSRX rearLeft = new WPI_TalonSRX(lbMotorPort);
		//WPI_TalonSRX frontRight = new WPI_TalonSRX(rfMotorPort);
		WPI_TalonSRX rearRight = new WPI_TalonSRX(rbMotorPort);

		drive = new DifferentialDrive(frontLeft, rearRight);
		frontLeft.setInverted(true);
		//rearLeft.setInverted(true);
		//frontRight.setInverted(true);
		rearRight.setInverted(true);
		// rightStick = new Joystick(JoystickInput);
		// leftStick = new Joystick(rightJoystickInput);
		controller1 = new Joystick(0);
    	controller2 = new Joystick(1);
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
	}

	@Override
	// Called every 20 milliseconds in teleop
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	
		drive.tankDrive(controller1.getRawAxis(1), controller1.getRawAxis(5));

		if(controller1.getRawButton(4) ) {
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
