package org.usfirst.frc.team568.robot.stronghold;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;
import org.usfirst.frc.team568.robot.subsystems.Arms;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2016;
import org.usfirst.frc.team568.robot.subsystems.Shooter2016;

/* TODO: replace with OpenCV
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;
*/

//import edu.wpi.first.wpilibj.CameraServer;]
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotBase {

	int session;
	//Image frame;
	double Pan;
	double Tilt;
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
	Timer time;
	// public double whichOne;
	// public boolean over;

	public double speed;

	// public double EncoderValue;

	protected static Robot instance;
	public OI oi;

	// AimingPID aim;

	public ArcadeDrive arcadeDrive;

	public Shooter2016 shooter;

	public Arms arms;

	public ReferenceFrame2016 referanceFrame2;
	Command autonomousCommand;

	/*
	 * double Pan; double Tilt; double BoxSize;
	 * 
	 * double KI; double KP; double KD; double TiltKP; double TiltKD; double
	 * TiltKI; double ErrSum; double Err2; double Err; double Pow; boolean LL;
	 * boolean LR;
	 * 
	 * double tiltErr; double tiltErr2; double tiltPow;
	 */

	public Robot() {
		instance = this;
		
		port("nudge", 8);
		port("leftFrontMotor", 4);
		port("leftBackMotor", 5);
		port("rightFrontMotor", 1);
		port("rightBackMotor", 2);

		port("upperLimmitSwitch", 3);
		port("lowerLimmitSwitch", 2);
		port("topLimmitSwitch", 0);
		port("bottomLimmitSwitch", 1);

		port("spike2", 1);
		port("spike1", 0);

		port("joy1Pos", 0);
		port("joy2Pos", 1);
		port("joy3Pos", 2);
		
		port("CrateBot.leftFront", 0);
		port("CrateBot.leftBack", 1);
		port("CrateBot.rightFront", 2);
		port("CrateBot.rightBack", 4);

		port("CrateBot.brake", 2);
		port("CrateBot.lifterMotor", 5);
		port("CrateBot.lowZoneMotor", 8);
		
		oi = new OI();
		// aim = new AimingPID(0.001, 0, 0);

		arcadeDrive = new ArcadeDrive(this);
		shooter = new Shooter2016(this);
		arms = new Arms(this);

		referanceFrame2 = new ReferenceFrame2016();
		time = new Timer();

	}

	@Override
	public void robotInit() {

		System.out.println("Robot Init");
		referanceFrame2.start();
		referanceFrame2.calabrateGyro();
		referanceFrame2.reset();

		/* NIVision is now an external dependency - re-enable if needed.
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		*/
		
		/*
		 * SmartDashboard.putNumber("P", 2.00); SmartDashboard.putNumber("I",
		 * 0.700); SmartDashboard.putNumber("D", 0);
		 * SmartDashboard.putNumber("TP", 5.000); SmartDashboard.putNumber("TI",
		 * 0); SmartDashboard.putNumber("TD", 0);
		 * SmartDashboard.putNumber("encoderValue", encoder.getDistance());
		 */

		SmartDashboard.putBoolean("Forward?", true);
		SmartDashboard.putNumber("Time?", 10);
		SmartDashboard.putNumber("Speed", .60);
		SmartDashboard.putNumber("Autonomous #", 1);
		SmartDashboard.putString("Event:", "Robot init");

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
		int autonomousNumber = (int) SmartDashboard.getNumber("Autonomous #", 1.0);
		switch(autonomousNumber) {
		case 1:
			autonomousCommand = new AutoOne();
			break;
		case 2:
			autonomousCommand = new AutoTwo();
			break;
		default:
			return;
		}
		referanceFrame2.reset();
		autonomousCommand.start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {

		// SmartDashboard.putNumber("POS X", referanceFrame2.getPos().x);
		// SmartDashboard.putNumber("POS Y", referanceFrame2.getPos().y);
		/// SmartDashboard.putNumber("Heading", referanceFrame2.getHeading());
		// SmartDashboard.putNumber("Acel Y", referanceFrame2.getAcel().y);
		// SmartDashboard.putNumber("Acel X", referanceFrame2.getAcel().x);
		// SmartDashboard.putNumber("Encoder", encoder.getDistance());
		/*
		 * KP = SmartDashboard.getNumber("P"); KI =
		 * SmartDashboard.getNumber("I"); KD = SmartDashboard.getNumber("D");
		 * TiltKP = SmartDashboard.getNumber("TP"); TiltKI =
		 * SmartDashboard.getNumber("TI"); TiltKD =
		 * SmartDashboard.getNumber("TD");
		 */

		Scheduler.getInstance().run();

		/* NIVision is now an external dependency - will rework if needed in the future
		NIVision.IMAQdxStartAcquisition(session);
		NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
		NIVision.IMAQdxGrab(session, frame, 1);
		NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
		CameraServer.getInstance().setImage(frame);
		*/

		/*
		 * SmartDashboard.putNumber("POS X", referanceFrame2.getPos().x);
		 * SmartDashboard.putNumber("POS Y", referanceFrame2.getPos().y);
		 * SmartDashboard.putNumber("Heading", referanceFrame2.getHeading());
		 * SmartDashboard.putNumber("Acel Y", referanceFrame2.getAcel().y);
		 * SmartDashboard.putNumber("Acel X", referanceFrame2.getAcel().x);
		 * SmartDashboard.putNumber("Encoder", encoder.getDistance());
		 * 
		 * KP = SmartDashboard.getNumber("P"); KI =
		 * SmartDashboard.getNumber("I"); KD = SmartDashboard.getNumber("D");
		 * TiltKP = SmartDashboard.getNumber("TP"); TiltKI =
		 * SmartDashboard.getNumber("TI"); TiltKD =
		 * SmartDashboard.getNumber("TD");
		 */

		/*
		 * NetworkTable server = NetworkTable.getTable("SmartDashboard"); try {
		 * System.out.println(server.getNumber("COG_X", 0.0));
		 * System.out.println(server.getNumber("COG_Y", 0.0)); } catch
		 * (TableKeyNotDefinedException ex) {
		 * 
		 * }
		 * 
		 * if (rightStick.getRawButton(3)) { Pan =
		 * SmartDashboard.getNumber("COG_X"); Tilt =
		 * SmartDashboard.getNumber("COG_Y"); BoxSize =
		 * SmartDashboard.getNumber("COG_BOX_SIZE"); if (Pan > 0) { Err2 = Err;
		 * Err = Pan - 320; ErrSum += Err; if (ErrSum > 1000) ErrSum = 1000; if
		 * (ErrSum < -1000) ErrSum = -1000; Pow = Err * (KP / 1000) + Pow * (KI
		 * / 1000) + (Err - Err2) * (KD / 1000);// Pos // turn
		 * Robot.tankDrive(-Pow, Pow); if (Err < 0) { LL = false; LR = true; }
		 * else { LL = true; LR = false; } SmartDashboard.putNumber("ERR", Err);
		 * SmartDashboard.putNumber("Pow", Pow);
		 * SmartDashboard.putNumber("Size", BoxSize);
		 * 
		 * if (Math.abs(Err) < 100) { // ErrSum = 0; if (BoxSize < 100)
		 * DarwinsRobot.tankDrive(-.5, -.5);
		 * 
		 * tiltErr = 240 - Tilt; tiltErr2 = tiltErr; tiltPow = tiltErr * (TiltKP
		 * / 1000) + tiltPow * (TiltKI / 1000)+ (tiltErr - tiltErr2) * (TiltKD /
		 * 1000); bob.set(tiltPow); sam.set(tiltPow);
		 * SmartDashboard.putNumber("TiltPow", tiltPow);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * else { bob.set(0); sam.set(0); if (LL) { DarwinsRobot.tankDrive(-.55,
		 * .55); } if (LR) { DarwinsRobot.tankDrive(.55, -.55); } }
		 * 
		 * }
		 */

	}

	@Override
	public void testPeriodic() {

	}

	public static Robot getInstance() {
		return instance;
	}
}
