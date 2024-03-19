package frc.team568.robot.crescendo.command;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.RotationCalc;
import frc.team568.robot.subsystems.SwerveSubsystem;

public class LookAtAmp extends Command{
	SwerveSubsystem drive;

	RotationCalc rotCalc;

	Pose2d lookAmp;

	PathConstraints constraints;

	public LookAtAmp(SwerveSubsystem drive){
		this.drive = drive;
	}

	public void initialize(){
		lookAmp = new Pose2d(drive.getPose().getX(), drive.getPose().getY(), rotCalc.getTargetAngle(false));
		constraints = new PathConstraints(3.0, 3.0, Units.degreesToRadians(540), Units.degreesToRadians(720)); //taken from default contraints in PP
		addRequirements(drive);
	}


	public void execute(){
		AutoBuilder.pathfindToPose(lookAmp, constraints, 0, 0);
	}

	public boolean isFinished(){
		return rotCalc.isPointingToTarget(false);
	}
	
}
