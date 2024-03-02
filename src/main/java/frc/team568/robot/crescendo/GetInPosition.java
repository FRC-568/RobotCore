package frc.team568.robot.crescendo;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;
import frc.team568.robot.subsystems.SwerveSubsystem;
import static frc.team568.robot.crescendo.Constants.SwerveConstants.kMaxSpeed;
import java.lang.Math;

public class GetInPosition {

	SwerveSubsystem swerveSub;
	JukeboxSubsystem jukebox;
	PivotSubsystem pivot;

	Command scorespeaker;

	Pose2d position;

	Pose3d shooterposition;

	double[] robCoords;
	double[] speakCoords;
	double[] yRestraints; 

	double angle;
	double maxAngle = 59; //THIS ANGLE IS IN DEGREES (1.03 radians, if needed). If changed, you will have to mess with line 77
	double tooClose = 0.92; //This measurement if from the subwoofer size from wall. The number is in meters, but can be converted to another metric if needed. Just make sure you change whatever else
	double wallHeight = 2.11; //Height of the wall(speaker) in meters, if changed to a different metric, change other varibales that may be in centimeters

	Rotation2d rotation;

	Rotation3d shooterRotation;

	public GetInPosition(){
		swerveSub = new SwerveSubsystem("crescendo", kMaxSpeed);
		scorespeaker = new ScoreSpeaker(jukebox, pivot);

		speakCoords[0] = 0;
		speakCoords[1] = 0; //TODO: Replace the placeholder coordinates (0 = X, 1 = Y) with proper coordinates about the location of the speaker. Avery should be working on getting that (I think)
		
		yRestraints[0] = speakCoords[0] + 6;
		yRestraints[1] = speakCoords[1] - 6; // 6 is just a placeholder for both. It is in meters

		if (isRobotInZone()){
			Commands.run((Runnable)scorespeaker, jukebox,pivot); // Unsure about the syntax if this line, sooo yeah
			System.out.print("Yippie");
		}
		else{
			goToZone();
		}
	}

	private Pose2d getPosition(){
		position = swerveSub.getPose();
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
		return rotation.getDegrees(); //If 'maxAngle' is changed to radians, change .getDegrees to .getRadians
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

	private double calcHyp() {
		return Math.hypot(checkTotDistance(), wallHeight);
	}

	private double checkShootAngle(){
		return Math.acos(checkTotDistance()/calcHyp()); // angle of robot's current position to the position of the TOP of the speaker
	}

	private double getRobotSpeakerOffset(){
		return Math.acos(checkXDistance()/checkTotDistance()); // angle of robot's position to the position of the speaker in general
	}

	private Boolean checkWithinYRestraints(){
		return ((yRestraints[0] > robCoords[1]) & (robCoords[1] > yRestraints[1]));
	}

	private Boolean checkDistanceGood(){
		return ((checkXDistance() > tooClose) & (yRestraints[0] > robCoords[1]) & (robCoords[1] > yRestraints[1]));
	}

	private Boolean checkAngleGood(){
		return (checkShootAngle() < maxAngle);
	}

	private Boolean isRobotInZone(){ 
		return (checkAngleGood() & checkDistanceGood());
	}

	private void pointToSpeaker(){ //Determines the best angle to rotate from. OpAM is the robot rotating clockwises, OpAP is the robot rotating counter clockwise
		double optimalAngleMinus = (getRot() - getRobotSpeakerOffset());
		double optimalAnglePlus = (getRot() + getRobotSpeakerOffset());
		if (optimalAngleMinus - optimalAnglePlus < 0){
			swerveSub.drive(0, 0, -optimalAngleMinus);
		}
		else{
			swerveSub.drive(0, 0, optimalAnglePlus);
		} 
	}

	private double getNeededYChange(){
		if (yRestraints[0] < robCoords[1]){
			return (robCoords[1] - yRestraints[1]); //If the robot is too far up in the field, go down
		}
		else{
			return (yRestraints[1] - robCoords[1]); // If the robot is too far down in the field, go up
		}
	}

	private void goToZone(){
		pointToSpeaker();
		while (isRobotInZone() == false){
			while (checkShootAngle() > maxAngle){  //While the robot can't make a shot into the speaker... (because of angle)
				swerveSub.drive(3, 0, 0); // 3 is a placeholder value
			}
			while (checkTotDistance() < tooClose){ //While the robot is too close to the speaker...
				swerveSub.drive(-2, 0, 0); // -2 is a placeholder value
			}
			while (checkWithinYRestraints() != true){ //While the robot is too far (on the y coordinate) from the speaker...
				swerveSub.drive(0,getNeededYChange(), 0);
			}
			pointToSpeaker();
			isRobotInZone();
		}
	}

	private Pose3d translateToShooter(){ // Unused method translating robot position to make jukebox position possible better results. 
		return new Pose3d(getPosition());// No real application for the method yet, but there could be idk ¯\_(ツ)_/¯
	}

	// **EVERYTHING HERE IS ASSUMING THAT THE NOTE WILL BE SHOT FROM THE ROBOT INTO A STRAIGHT (enough) LINE SO THAT IT CAN MAKE IT INTO THE SPEAKER**
}
