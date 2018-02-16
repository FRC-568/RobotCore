package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.commands.BringLiftDown;
import org.usfirst.frc.team568.robot.commands.Intake;
import org.usfirst.frc.team568.robot.commands.LiftBlock;
import org.usfirst.frc.team568.robot.commands.Outtake;
import org.usfirst.frc.team568.robot.subsystems.BlockIntake;
import org.usfirst.frc.team568.robot.subsystems.BlockLift2018;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	Command autonomousCommand;
	public Drive2018 driveTrain;
	public BlockLift2018 blockLift;
	public BlockIntake blockIntake;
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

		instance = this;
		oi = new OI();
		driveTrain = new Drive2018(this);
		blockLift = new BlockLift2018(this);
		blockIntake = new BlockIntake(this);

	}

	@Override
	public void robotInit() {
		oi.liftUp.whileHeld(new LiftBlock());
		oi.liftDown.whileHeld(new BringLiftDown());
		oi.intake.whileHeld(new Intake());
		oi.outtake.whileHeld(new Outtake());
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
