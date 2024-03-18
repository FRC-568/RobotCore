package frc.team568.robot.crescendo;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.subsystems.SwerveSubsystem;
import java.lang.Math;

public class RotationCalc {

	SwerveSubsystem drive;

	Command scorespeaker;

	Pose2d position;

	Pose3d shooterposition;

	double[] robCoords;
	double[] speakCoords;

	Rotation2d rotation;
	Rotation3d shooterRotation;

	Rotation2d speakerRot;

	public RotationCalc(SwerveSubsystem drive){

		speakCoords[0] = Location.SPEAKER_TARGET.getTranslation().getX();
		speakCoords[0] = Location.SPEAKER_TARGET.getTranslation().getZ();

		speakerRot = new Rotation2d(speakCoords[0], speakCoords[1]);

	}

	private Pose2d getPosition(){
		position = drive.getPose();
		return position;
	}

	private double getXPos(){
		robCoords[0] = getPosition().getX();
		return robCoords[0];
	}

	private double getYPos(){
		robCoords[1] = getPosition().getY();
		return robCoords[1];
	}

	private double getRot(){
		rotation = getPosition().getRotation();
		return rotation.getDegrees();
	}

	private double checkXDistance(){
		return getXPos() - speakCoords[0];
	}

	private double checkYDistance(){
		return getYPos() - speakCoords[1];
	}

	private double checkTotDistance(){
		return Math.hypot(checkXDistance(), checkYDistance()); //Distance between the robot and the speaker in a straight line
	}

	private double getRobotSpeakerOffset(){
		return Math.acos(checkXDistance()/checkTotDistance()); // angle of robot's position to the position of the speaker in general
	}

	/*public Rotation2d getTargetAngle(){
		double angle = (getRobotSpeakerOffset() + getRot());
		if (angle > 180){ // Path planner only accepts ranges from -179 to 180.
			angle = angle - 360;
		}
		else if (angle <= -180){
			angle = angle + 360;
		}
		return Rotation2d.fromDegrees(angle);
	}*/

	/*public Rotation2d getTargetAngle(){
		return targetrot.minus(Rotation2d.fromDegrees(getRot()));
	}*/                

	// ^^ These commented out methods are potential alternatives incase the current getTargetAngle does not work

	public Rotation2d getTargetAngle(){
		double angle = speakerRot.plus(Rotation2d.fromDegrees(getRot())).getDegrees();
		if (angle > 180){ // Path planner only accepts ranges from -179 to 180.
			angle = angle - 360;
		}
		else if (angle <= -180){
			angle = angle + 360;
		}
		return Rotation2d.fromDegrees(angle);
	}

	public boolean isPointingToSpeaker(){
		return (
			getTargetAngle().getDegrees() + 3 > getRot() || getTargetAngle().getDegrees() - 3 < getRot()
		); // Idea is to allow the robot an error of 3 (6 in total) degrees so that it isn't constantly trying to get the 'perfect' rotation 
	}
}
