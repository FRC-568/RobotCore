package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.steamworks.Robot;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveManual2017 extends Command {
	public final DriveTrain arcadeDrive;
	public Counter count;
	// CrateLifter lifter = Robot.getInstance().crateLifter;

	public ArcadeDriveManual2017() {
		arcadeDrive = Robot.getInstance().driveTrain;
		requires(arcadeDrive);
		count = new Counter();

	}

	@Override
	protected void initialize() {
		count.reset();

		//count.setUpSource(RobotMap.laser1);


	}

	@Override
	protected void execute() {
		arcadeDrive.arcadeDrive();

		SmartDashboard.putNumber("Count", count.get());

		// lifter.lift();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
	}

}
