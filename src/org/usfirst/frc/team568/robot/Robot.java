package org.usfirst.frc.team568.robot;

import org.usfirst.frc.team568.robot.commands.AutonomousTest;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;
import org.usfirst.frc.team568.robot.subsystems.Arms;
import org.usfirst.frc.team568.robot.subsystems.CrateLifter;
import org.usfirst.frc.team568.robot.subsystems.Flipper;
import org.usfirst.frc.team568.robot.subsystems.MeccanumDrive;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;
import org.usfirst.frc.team568.robot.subsystems.Shooter;
import org.usfirst.frc.team568.robot.subsystems.TankDrive;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Encoder encoder;
	int session;
	Image frame;
	double BoxSize;
	double KI;
	double KP;
	double KD;
	double TiltKP;
	double TiltKD;
	double TiltKI;
	double ErrSum;
	double Err2;
	double Err;
	double Pow;
	boolean LL;
	boolean LR;
	double tiltErr;
	double tiltErr2;
	double tiltPow;
	// public double EncoderValue;

	protected static Robot instance;
	public OI oi;

	public Arms arms;
	public MeccanumDrive meccanumDrive;
	public Shooter shooter;
	public ArcadeDrive arcadeDrive;
	public TankDrive tankDrive;
	public Flipper flipper;
	public CrateLifter crateLifter;
	public ReferenceFrame referenceframe;
	public ReferenceFrame2 referanceFrame2;
	Command autonomousCommand;
	// SendableChooser chooser;
	// CameraServer cam0;
	Compressor comp;

	// double speed = 0;
	// double inches = 0;
	// double timeout = 0;
	// double encoderValue = 0;

	public Robot() {
		instance = this;
		oi = new OI();
		// arcadeDrive = new ArcadeDrive();
		// meccanumDrive = new MeccanumDrive();
		tankDrive = new TankDrive();
		shooter = new Shooter();
		arms = new Arms();
		// referenceframe = new ReferenceFrame();
		referanceFrame2 = new ReferenceFrame2();
		// flipper = new Flipper();
		// crateLifter = new CrateLifter();
		encoder = new Encoder(8, 9);
		// cam0 = CameraServer.getInstance();
		// cam0.startAutomaticCapture("cam1");
		comp = new Compressor();
		referanceFrame2.start();
		referanceFrame2.calabrateGyro();
		// SmartDashboard.putNumber("EncoderValue", encoder.getDistance());
		// SmartDashboard.putNumber("IMUCurrentPosition",
		// referenceframe.imu.getDisY());
		// referanceFrame2.threshold = SmartDashboard.getNumber("Threshold");
		// SmartDashboard.putNumber("Threshold", referanceFrame2.threshold);

	}

	@Override
	public void robotInit() {
		encoder.reset();
		// chooser = new SendableChooser();
		// SmartDashboard.putData("Auto mode", chooser);
		comp.start();
		// Robot.getInstance().referenceframe.imu.calibrate();
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		// NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		SmartDashboard.putNumber("P", 0.700);
		SmartDashboard.putNumber("I", 0.700);
		SmartDashboard.putNumber("D", 0);
		SmartDashboard.putNumber("TP", 0.500);
		SmartDashboard.putNumber("TI", 0);
		SmartDashboard.putNumber("TD", 0);
		SmartDashboard.putNumber("encoderValue", encoder.getDistance());
		SmartDashboard.getNumber("Autonomous #");

		// referanceFrame2.threshold = SmartDashboard.getNumber("Threshold");
		encoder.reset();
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {

		comp.start();
		// autonomousCommand = (Command) chooser.getSelected();
		double speed = SmartDashboard.getNumber("speed");
		double inches = SmartDashboard.getNumber("inches");
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		autonomousCommand = new AutonomousTest(speed, inches);
		autonomousCommand.start();
		// schedule the autonomous command (example)

		/*
		 * this.autonomousCommand = ((Command) this.chooser.getSelected()); if
		 * (this.autonomousCommand != null) { this.autonomousCommand.start();
		 * 
		 * }
		 */

		// this.autonomousCommand = ((Command) this.chooser.getSelected());
		// if (autonomousCommand != null) {
		/// autonomousCommand.start();
		// }
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		// SmartDashboard.putNumber("speed", .5);
		// SmartDashboard.putNumber("inches", 1);
		// SmartDashboard.putNumber("timeOut", 5);
		// SmartDashboard.putNumber("IMUCurrentPosition",
		// referenceframe.imu.getDisY());

	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		referanceFrame2.reset();

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("speed", .5);
		SmartDashboard.putNumber("inches", 1);
		SmartDashboard.putNumber("timeOut", 5);
		// SmartDashboard.putNumber("IMUCurrentPosition",
		// referenceframe.imu.getDisY() * 1000);

		SmartDashboard.putNumber("POS X", referanceFrame2.getPos().x);
		SmartDashboard.putNumber("POS Y", referanceFrame2.getPos().y);
		SmartDashboard.putNumber("Heading", referanceFrame2.getHeading());
		SmartDashboard.putNumber("Acel Y", referanceFrame2.getAcel().y);
		SmartDashboard.putNumber("Acel X", referanceFrame2.getAcel().x);
		SmartDashboard.putNumber("Encoder", encoder.getDistance());

		KP = SmartDashboard.getNumber("P");
		KI = SmartDashboard.getNumber("I");
		KD = SmartDashboard.getNumber("D");
		TiltKP = SmartDashboard.getNumber("TP");
		TiltKI = SmartDashboard.getNumber("TI");
		TiltKD = SmartDashboard.getNumber("TD");
		/////// aquire
		NIVision.IMAQdxStartAcquisition(session);
		/**
		 * grab an image, draw the circle, and provide it for the camera server
		 * which will in turn send it to the dashboard.
		 */
		NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

		// referanceFrame2.threshold = SmartDashboard.getNumber("Threshold");
		NIVision.IMAQdxGrab(session, frame, 1);
		NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
		CameraServer.getInstance().setImage(frame);
		// NIVision.IMAQdxStopAcquisition(session);

	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
