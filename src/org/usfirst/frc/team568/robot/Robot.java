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
	public ArcadeDrive drive;
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
		drive = new ArcadeDrive();
		meccanumDrive = new MeccanumDrive();
		referenceframe = new ReferenceFrame();
		flipper = new Flipper();
		crateLifter = new CrateLifter();
		cam0 = CameraServer.getInstance();
		cam0.startAutomaticCapture("cam0");
		comp = new Compressor();
		SmartDashboard.putNumber("speed", .5);
		SmartDashboard.putNumber("inches", 70);
		SmartDashboard.putNumber("timeOut", 5);
	}

	@Override
	public void robotInit() {
		this.chooser = new SendableChooser();

		SmartDashboard.putData("Auto mode", this.chooser);
		this.comp.start();
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
		autonomousCommand = (Command) chooser.getSelected();
		double speed = SmartDashboard.getNumber("speed");
		double inches = SmartDashboard.getNumber("inches");
		double timeout = SmartDashboard.getNumber("Timeout");

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		/*
		 * this.autonomousCommand = ((Command) this.chooser.getSelected()); if
		 * (this.autonomousCommand != null) { this.autonomousCommand.start();
		 * 
		 * }
		 */
		new AutonomousTest(speed, inches, timeout);
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

	}

	@Override
	public void teleopInit() {
		if (this.autonomousCommand != null) {
			this.autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		this.drive.manualDrive();
	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
