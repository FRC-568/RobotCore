package frc.team568.robot.powerup;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.WinchClimber;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

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
		super("powerup");
		
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
	}

	@Override
	public void robotInit() {
		// SmartDashboard.putNumber("Turn.P", 0.05);
		// SmartDashboard.putNumber("Turn.I", 0.003);
		// SmartDashboard.putNumber("Turn.D", 0.0);
		compressor.start();
		cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setResolution(360, 720);
		// cam.setFPS(7);
		driveTrain.calGyro();

		SmartDashboard.putNumber("Robot Position: ", 0);
		SmartDashboard.setPersistent("Robot Position: ");

		oi.liftUp.whileHeld(blockLift.getCommandRaise());
		oi.liftDown.whileHeld(blockLift.getCommandLower());
		oi.blockIn.whileHeld(blockIntake.getCommandBlockLiftIn());
		oi.blockIn2.whileHeld(blockIntake.getCommandBlockLiftIn());
		oi.blockOut.whileHeld(blockIntake.getCommandBlockLiftOut());
		oi.blockOut2.whileHeld(blockIntake.getCommandBlockLiftOut2());

		oi.blockGrab.whileHeld(blockIntake.blockGrabCommand());
		oi.armIn.whileHeld(blockIntake.getCommandArmIn());
		oi.armOut.whileHeld(blockIntake.getCommandArmOut());

		oi.climb.whileHeld(new ClimbWithWinch());
		oi.unClimb.whileHeld(new UnClimb());

		oi.liftUpA.whileHeld(blockLift.getCommandRaise());
		oi.liftDownA.whileHeld(blockLift.getCommandLower());
		oi.blockLiftIn.whileHeld(blockIntake.getCommandBlockLiftIn());
		oi.blockLiftOut.whileHeld(blockIntake.getCommandBlockLiftOut());
		oi.climbA.whileHeld(new ClimbWithWinch());
		oi.unClimbA.whileHeld(new UnClimb());
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
		CommandScheduler.getInstance().run();
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
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		int robotPos = (int) SmartDashboard.getNumber("Robot Position: ", 0);
		int scalePos = 0;
		int switchPos = 0;

		if (gameData.length() > 0) {
			if (gameData.charAt(0) == 'L') {
				switchPos = 1;
			} else {
				switchPos = 2;
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
			autonomousCommand.schedule();;
	}

	@Override
	public void robotPeriodic() {
		SmartDashboard.getNumber("Robot Position: ", 0);
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		CommandScheduler.getInstance().run();
	}

	public static Robot getInstance() {
		return instance;
	}

}
