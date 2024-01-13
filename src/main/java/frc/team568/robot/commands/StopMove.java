package frc.team568.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class StopMove extends Command {

	private final TwoMotorDrive drive;
	private boolean isFin = false;

	public StopMove(TwoMotorDrive drive) {

		this.drive = drive;

	}

	@Override
	public void initialize() {
		
		isFin = false;

	}

	@Override
	public void execute() {

		// Set motor powers
		drive.setLeft(0);
		drive.setRight(0);

		isFin = true;
	
	}

	@Override
	public boolean isFinished() {

		return isFin;
	
	}

	@Override
	public void end(boolean interrupted) {
		
	}
	
}
