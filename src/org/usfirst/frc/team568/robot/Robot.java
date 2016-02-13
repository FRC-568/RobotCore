package org.usfirst.frc.team568.robot;

import org.usfirst.frc.team568.robot.commands.AutonomousTest;
import org.usfirst.frc.team568.robot.subsystems.ArcadeDrive;
import org.usfirst.frc.team568.robot.subsystems.CrateLifter;
import org.usfirst.frc.team568.robot.subsystems.Flipper;
import org.usfirst.frc.team568.robot.subsystems.GreenHorn;
import org.usfirst.frc.team568.robot.subsystems.MeccanumDrive;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	protected static Robot instance;
	public OI oi;

	public MeccanumDrive drive;
	public ArcadeDrive arcadeDrive;
	public GreenHorn shooter;
	public Flipper flipper;
	public CrateLifter crateLifter;
	public ReferenceFrame referenceframe;
	public MeccanumDrive meccanumDrive;
	Command autonomousCommand;
	SendableChooser chooser;
	CameraServer cam0;
	Compressor comp;
	double speed = 0;
	double inches = 0;
	double timeout = 0;

	public Robot() {
		instance = this;
		oi = new OI();
		// arcadeDrive = new ArcadeDrive();

		referenceframe = new ReferenceFrame();
		drive = new MeccanumDrive();
		referenceframe = new ReferenceFrame();
		flipper = new Flipper();
		crateLifter = new CrateLifter();
		cam0 = CameraServer.getInstance();
		cam0.startAutomaticCapture("cam0");
		comp = new Compressor();
		SmartDashboard.putNumber("speed", .5);
		SmartDashboard.putNumber("inches", 20);
		SmartDashboard.putNumber("timeOut", 5);
		SmartDashboard.putNumber("IMUCurrentPosition", referenceframe.imu.getDisY());
	}

	@Override
	public void robotInit() {
		chooser = new SendableChooser();
		SmartDashboard.putData("Auto mode", chooser);
		comp.start();
		Robot.getInstance().referenceframe.imu.calibrate();
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
		SmartDashboard.putNumber("speed", .5);
		SmartDashboard.putNumber("inches", 1);
		SmartDashboard.putNumber("timeOut", 5);
		SmartDashboard.putNumber("IMUCurrentPosition", referenceframe.imu.getDisY());
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
		drive.manualDrive();
		SmartDashboard.putNumber("speed", .5);
		SmartDashboard.putNumber("inches", 1);
		SmartDashboard.putNumber("timeOut", 5);
		SmartDashboard.putNumber("IMUCurrentPosition", referenceframe.imu.getDisY() * 1000);
	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
