package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.ArmGrab;
import org.usfirst.frc.team568.robot.commands.ArmOpen;
import org.usfirst.frc.team568.robot.commands.BlockIn;
import org.usfirst.frc.team568.robot.commands.BlockOut;
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

		port("lift", 5);

		port("intakeOne", 6);
		port("intakeTwo", 7);
		port("intakeArmL", 2);
		port("intakeArmR", 3);
		port("armMotorL", 0);
		port("armMotorR", 1);

		port("climber", 8);

		instance = this;
		oi = new OI();
		driveTrain = new DriveTrain2018(this);
		addSubsystem(DriveTrain2018.class, driveTrain);
		blockLift = new BlockLift2018(this);
		blockIntake = new BlockHandler(this);
		climber = new WinchClimber(this);

	}

	@Override
	public void robotInit() {
		driveTrain.calGyro();

		oi.liftUp.whileHeld(new LiftBlock());
		oi.liftDown.whileHeld(new BringLiftDown());
		oi.blockIn.whileHeld(new BlockIn());
		oi.blockOut.whileHeld(new BlockOut());
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
		if (gameData.length() > 0) {
			if (gameData.charAt(1) == 'L') {
				autonomousCommand = new AutoTwo(this);
			} else {
				autonomousCommand = new AutoOne(this);
			}
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

	}

	@Override
	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return instance;
	}
}
