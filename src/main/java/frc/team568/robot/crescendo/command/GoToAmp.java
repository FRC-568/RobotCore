package frc.team568.robot.crescendo.command;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.subsystems.SwerveSubsystem;

public class GoToAmp extends Command{

	SwerveSubsystem drive;
	PathConstraints constraints;
	Pose2d targetAmp;

	public GoToAmp(SwerveSubsystem drive){
		this.drive = drive;
		addRequirements(drive);
	}
	
	public void initialize(){
		targetAmp = new Pose2d(1.88, 7.23, Rotation2d.fromDegrees(90));
		constraints = new PathConstraints(3.0, 3.0, Units.degreesToRadians(540), Units.degreesToRadians(720));

	}

	public void execute(){
		AutoBuilder.pathfindToPose(targetAmp, constraints, 0, 0);
	}

	public boolean isFinished(){
		return true;
	}
	
}
