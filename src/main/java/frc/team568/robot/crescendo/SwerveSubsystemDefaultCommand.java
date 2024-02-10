// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;

/** Add your docs here. */

public class SwerveSubsystemDefaultCommand extends Command{
	public final SwerveSubsystem drive;

	public SwerveSubsystemDefaultCommand(final SwerveSubsystem drive){
		this.drive = drive;
		addRequirements(drive);
}
	

	@Override
	public void execute(){
		// Get the x speed. We are inverting this because Xbox controllers return
		// negative values when we push forward.
		final var xSpeed = OI.Axis.swerveForward.getAsDouble();

		// Get the y speed or sideways/strafe speed. We are inverting this because
		// we want a positive value when we pull to the left. Xbox controllers
		// return positive values when you pull to the right by default.
		final var ySpeed = OI.Axis.swerveLeft.getAsDouble();

		// Get the rate of angular rotation. We are inverting this because we want a
		// positive value when we pull to the left (remember, CCW is positive in
		// mathematics). Xbox controllers return positive values when you pull to
		// the right by default.
		final var rot = OI.Axis.swerveCCW.getAsDouble();

		drive.drive(xSpeed, ySpeed, rot);
	}

}
