package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.ArmGrab;
import org.usfirst.frc.team568.robot.commands.ArmOpen;
import org.usfirst.frc.team568.robot.commands.BlockIn;
import org.usfirst.frc.team568.robot.commands.BlockOut;
import org.usfirst.frc.team568.robot.commands.BlockOut2;
import org.usfirst.frc.team568.robot.commands.BringLiftDown;
import org.usfirst.frc.team568.robot.commands.ClimbWithWinch;
import org.usfirst.frc.team568.robot.commands.LiftBlock;
import org.usfirst.frc.team568.robot.commands.UnClimb;
import org.usfirst.frc.team568.robot.subsystems.BlockHandler;
import org.usfirst.frc.team568.robot.subsystems.BlockLift2018;
import org.usfirst.frc.team568.robot.subsystems.WinchClimber;

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
		port("intakeArmL", 2);
		port("intakeArmR", 3);
		port("armMotorL", 0);
		port("armMotorR", 1);

		port("climber", 8);

		instance = this;
		oi = new OI();
		driveTrain = addSubsystem(DriveTrain2018::new);
		blockLift = addSubsystem(BlockLift2018::new);
		blockIntake = addSubsystem(BlockHandler::new);
		climber = addSubsystem(WinchClimber::new);

	}

	@Override
	public void robotInit() {
		SmartDashboard.putNumber("Robot Position: ", 0);
		driveTrain.calGyro();

		oi.liftUp.whileHeld(blockLift.getCommandRaise());
		oi.liftDown.whileHeld(blockLift.getCommandLower());
		oi.blockIn.whileHeld(new BlockIn());
		oi.blockOut.whileHeld(new BlockOut2());
		oi.blockOut2.whileHeld(new BlockOut());
		oi.armGrab.whileHeld(new ArmGrab());
		oi.armOpen.whileHeld(new ArmOpen());
		oi.climb.whileHeld(new ClimbWithWinch());
		oi.unClimb.whileHeld(new UnClimb());
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

		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		int robotPos = (int) SmartDashboard.getNumber("Robot Position: ", 0);
		int scalePos = 0;

		if (gameData.length() > 0) {
			if (gameData.charAt(1) == 'L') {
				scalePos = 1;
			} else {
				scalePos = 2;
			}
		}
		if (robotPos != 0 && scalePos != 0) {
			autonomousCommand = new AutoOne(this, scalePos, robotPos);
		}
		if (autonomousCommand != null)
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

	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
