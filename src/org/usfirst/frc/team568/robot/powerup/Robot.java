package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.ClimbWithWinch;
import org.usfirst.frc.team568.robot.commands.UnClimb;
import org.usfirst.frc.team568.robot.subsystems.BlockHandler;
import org.usfirst.frc.team568.robot.subsystems.BlockLift2018;
import org.usfirst.frc.team568.robot.subsystems.WinchClimber;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotBase {
	Command autonomousCommand;
	public DriveTrain2018 driveTrain;
	public BlockLift2018 blockLift;
	public BlockHandler blockIntake;
	public WinchClimber climber;
	public UsbCamera cam;
	public Compressor compressor;

	public Command testCommand;
	public double turnAngle;

	public DriverStation.Alliance color;

	public OI oi;

	protected static Robot instance;

	public Robot() {
		port("leftFrontMotor", 1);
		port("leftBackMotor", 2);
		port("rightFrontMotor", 3);
		port("rightBackMotor", 4);

		port("liftMotor", 5);
		port("liftEncoderA", 0);
		port("liftEncoderB", 1);

		port("intakeOne", 6);
		port("intakeTwo", 7);

		port("climber", 8);

		port("extensionO", 1);
		port("extensionI", 0);
		port("grabberO", 2);
		port("grabberI", 3);

		port("blinkin", 9);

		instance = this;
		oi = new OI();
		driveTrain = addSubsystem(DriveTrain2018::new);
		blockLift = addSubsystem(BlockLift2018::new);
		blockIntake = addSubsystem(BlockHandler::new);
		climber = addSubsystem(WinchClimber::new);
		compressor = new Compressor();

		color = DriverStation.getInstance().getAlliance();

	}

	@Override
	public void robotInit() {
		SmartDashboard.putNumber("Drive.P", 0.004);
		SmartDashboard.putNumber("Drive.I", 0.001);
		SmartDashboard.putNumber("Drive.D", 0.004);

		SmartDashboard.putNumber("Turn.P", 0.1);
		SmartDashboard.putNumber("Turn.I", 0.01);
		SmartDashboard.putNumber("Turn.D", 0.0);
		compressor.start();
		cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setResolution(360, 720);
		// cam.setFPS(7);
		driveTrain.calGyro();

		driveTrain.blinkin.set(-.99);
		SmartDashboard.putNumber("Robot Position: ", 0);
		SmartDashboard.setPersistent("Robot Position: ");

		oi.liftUp.whileHeld(blockLift.getCommandRaise());
		oi.liftDown.whileHeld(blockLift.getCommandLower());
		oi.blockIn.whileHeld(blockIntake.getCommandBlockLiftIn());
		oi.blockOut.whileHeld(blockIntake.getCommandBlockLiftOut());
		oi.blockOut2.whileHeld(blockIntake.getCommandBlockLiftOut2());

		oi.blockGrab.whileHeld(blockIntake.blockGrabCommand());
		oi.armIn.whileHeld(blockIntake.getCommandArmIn());
		oi.armOut.whileHeld(blockIntake.getCommandArmOut());

		oi.climb.whileHeld(new ClimbWithWinch());
		oi.unClimb.whileHeld(new UnClimb());

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

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {

		if (color == DriverStation.Alliance.Blue)

		{
			driveTrain.blinkin.set(.87);
		} else {
			driveTrain.blinkin.set(.61);
		}

		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		int robotPos = (int) SmartDashboard.getNumber("Robot Position: ", 0);
		int scalePos = 0;
		int switchPos = 0;

		if (gameData.length() > 0) {
			if (gameData.charAt(0) == 'L') {
				scalePos = 1;
			} else {
				scalePos = 2;
			}
		}
		if (gameData.length() > 0) {
			if (gameData.charAt(1) == 'L') {
				scalePos = 1;
			} else {
				scalePos = 2;
			}
		}
		if (robotPos != 0 && scalePos != 0) {
			autonomousCommand = new AutoOne(this, scalePos, switchPos, robotPos);
		} else {
			autonomousCommand = new AutoTwo(this);
		}
		if (autonomousCommand != null)
			autonomousCommand.start();

	}

	@Override
	public void robotPeriodic() {
		SmartDashboard.getNumber("Robot Position: ", 0);
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

	}

	@Override
	public void teleopInit() {

		if (color == DriverStation.Alliance.Blue)

		{
			driveTrain.blinkin.set(.87);
		} else {
			driveTrain.blinkin.set(.61);
		}

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

	}

	public static Robot getInstance() {
		return instance;
	}

}
