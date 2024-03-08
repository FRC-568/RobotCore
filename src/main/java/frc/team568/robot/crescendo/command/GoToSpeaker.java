package frc.team568.robot.crescendo.command;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

public class GoToSpeaker extends Command{

	Pose2d targetSpeaker;
	PathConstraints constraints;

	public void initialize(){
		targetSpeaker =  new Pose2d(1.57, 5.53, Rotation2d.fromDegrees(180)); // X and Y taken from grid on pathfinder
		constraints = new PathConstraints(3.0, 3.0, Units.degreesToRadians(540), Units.degreesToRadians(720)); //taken from default contraints in PP
	}

	public void execute(){
		AutoBuilder.pathfindToPose(targetSpeaker, constraints, 0, 0);
	}

	public boolean isFinished(){
		return true; 
	}
	
}
