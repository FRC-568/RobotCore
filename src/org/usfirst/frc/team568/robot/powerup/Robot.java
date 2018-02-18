package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.BlockIn;
import org.usfirst.frc.team568.robot.commands.BlockOut;
import org.usfirst.frc.team568.robot.commands.BringLiftDown;
import org.usfirst.frc.team568.robot.commands.LiftBlock;
import org.usfirst.frc.team568.robot.subsystems.BlockHandler;
import org.usfirst.frc.team568.robot.subsystems.BlockLift2018;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	Command autonomousCommand;
	public DriveTrain2018 driveTrain;
	public BlockLift2018 blockLift;
	public BlockHandler blockIntake;
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
		port("intakeArmL", 0);
		port("intakeArmR", 1);
		port("armMotorL", 2);
		port("armMotorR", 3);

		instance = this;
		oi = new OI();
		driveTrain = new DriveTrain2018(this);
		addSubsystem(DriveTrain2018.class, driveTrain);
		blockLift = new BlockLift2018(this);
		blockIntake = new BlockHandler(this);

	}

	@Override
	public void robotInit() {
		driveTrain.calGyro();

		oi.liftUp.whileHeld(new LiftBlock());
		oi.liftDown.whileHeld(new BringLiftDown());
		oi.intake.whileHeld(new BlockIn());
		oi.outtake.whileHeld(new BlockOut());
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
		autonomousCommand = new AutoOne(this);

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
