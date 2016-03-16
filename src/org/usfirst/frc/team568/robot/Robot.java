package org.usfirst.frc.team568.robot;

import org.usfirst.frc.team568.robot.commands.AutoOne;
import org.usfirst.frc.team568.robot.commands.AutoTwo;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;
import org.usfirst.frc.team568.robot.subsystems.Arms;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;
import org.usfirst.frc.team568.robot.subsystems.Shooter;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	protected static Robot instance;
	public OI oi;

	public ArcadeDrive arcadeDrive;
	public Shooter shooter;
	public Arms arms;
	public ReferenceFrame2 referanceFrame2;
	Command autonomousCommand;

	int session;
	Image frame;

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
		oi = new OI();

		arcadeDrive = new ArcadeDrive();
		shooter = new Shooter();
		arms = new Arms();

		referanceFrame2 = new ReferenceFrame2();

	}

	@Override
	public void robotInit() {
		System.out.println("Robot Init");
		referanceFrame2.start();
		referanceFrame2.calabrateGyro();
		referanceFrame2.reset();

		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		/*
		 * SmartDashboard.putNumber("P", 2.00); SmartDashboard.putNumber("I",
		 * 0.700); SmartDashboard.putNumber("D", 0);
		 * SmartDashboard.putNumber("TP", 5.000); SmartDashboard.putNumber("TI",
		 * 0); SmartDashboard.putNumber("TD", 0);
		 * SmartDashboard.putNumber("encoderValue", encoder.getDistance());
		 */

		SmartDashboard.putNumber("How Long?", 12);
		SmartDashboard.putBoolean("Forward?", true);
		SmartDashboard.putNumber("Time?", 12);
		SmartDashboard.putNumber("Speed", .50);
		SmartDashboard.putNumber("Autonomous #?", 1);

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
		if (SmartDashboard.getNumber("Autonomous #") == 1) {
			autonomousCommand = new AutoOne();
		} else if (SmartDashboard.getNumber("Autonomous #") == 2) {
			autonomousCommand = new AutoTwo();
		}

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

		Scheduler.getInstance().run();

		NIVision.IMAQdxStartAcquisition(session);
		NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
		NIVision.IMAQdxGrab(session, frame, 1);
		NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
		CameraServer.getInstance().setImage(frame);

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

	// NIVision.IMAQdxStopAcquisition(session);

	@Override
	public void testPeriodic() {

	}

	public static Robot getInstance() {
		return instance;
	}
}
