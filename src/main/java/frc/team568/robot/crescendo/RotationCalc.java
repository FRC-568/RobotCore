package frc.team568.robot.crescendo;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.subsystems.SwerveSubsystem;

public class RotationCalc {

	SwerveSubsystem drive;

	Command scorespeaker;

	Pose3d shooterposition;

	Translation3d speakerTranslation;
	Translation3d ampTranslation;
	Translation3d target;


	Rotation2d speakerRot;
	Rotation2d ampRot;
	Rotation2d targetRot;
	Rotation2d botRot;


	public RotationCalc(SwerveSubsystem drive){		
		speakerRot = new Rotation2d(Location.SPEAKER_TARGET.getTranslation().getX(), Location.AMP_START.getTranslation().getZ());
		ampRot = new Rotation2d(Location.AMP_TARGET.getTranslation().getX(), Location.AMP_TARGET.getTranslation().getZ());

		speakerTranslation = Location.SPEAKER_TARGET.getTranslation();
		ampTranslation = Location.AMP_TARGET.getTranslation();
	}

	private double getRot(){
		botRot = drive.getPose().getRotation();
		return botRot.getDegrees();
	}

	public double checkTotDistance(boolean isSpeaker){
		target = isSpeaker? speakerTranslation:ampTranslation;
		return Math.hypot((drive.getPose().getX() - target.getX()) , (drive.getPose().getY()-target.getZ())); //Distance between the robot and the speaker in a straight line
	}

	public Rotation2d getTargetAngle(boolean isSpeakerRot){
		targetRot = isSpeakerRot? speakerRot:ampRot;

		double angle = (targetRot.plus((drive.getPose().getRotation()))).getDegrees();
		if (angle > 180){ // Path planner only accepts ranges from -179 to 180.
			angle = angle - 360;
		}
		else if (angle <= -180){
			angle = angle + 360;
		}
		return Rotation2d.fromDegrees(angle);
	}

	public boolean isPointingToTarget(boolean isSpeakerRot){
		return (
			getTargetAngle(isSpeakerRot).getDegrees() + 3 > getRot() || getTargetAngle(isSpeakerRot).getDegrees() - 3 < getRot()
		); // Give the robot an error allowance of 3 (total 6) degrees. Might be too much
	}
}
