package org.usfirst.frc.team568.robot.bart;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class DriveForward extends TimedCommand {
	public WestCoastDrive drive;
 
	public DriveForward(final WestCoastDrive drive, double time) {
		super(time);	
		this.drive = drive;	
	}
	@Override
	public void initialize() {
		drive.driveForward();
	}
	@Override
	public void end() {
		drive.stop();
	}
}
