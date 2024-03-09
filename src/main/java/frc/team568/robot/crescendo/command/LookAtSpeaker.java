package frc.team568.robot.crescendo.command;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.RotationCalc;
import frc.team568.robot.subsystems.SwerveSubsystem;

public class LookAtSpeaker extends Command {
	SwerveSubsystem drive;
	RotationCalc rotCalc;

	Pose2d lookSpeaker;
	PathConstraints constraints;
	public LookAtSpeaker(SwerveSubsystem drive){
		this.drive = drive;
	}
	public void initialize(){
		//lookSpeaker = new Pose2d(drive.getPose().getX(), drive.getPose().getY(), Rotation2d.fromDegrees(180));
		lookSpeaker = new Pose2d(drive.getPose().getX(), drive.getPose().getY(), rotCalc.pointToSpeaker());
		constraints = new PathConstraints(3.0, 3.0, Units.degreesToRadians(540), Units.degreesToRadians(720)); //taken from default contraints in PP
		addRequirements(drive);
	}
	public void execute(){
		AutoBuilder.pathfindToPose(lookSpeaker, constraints, 0, 0);
	}
	public boolean isFinished(){
		return true; 
	}
}	