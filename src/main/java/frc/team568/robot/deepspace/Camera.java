package frc.team568.robot.deepspace;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Camera {
	//TargetTracker1 cameraFeed;
	NetworkTable dataToSendTable = NetworkTableInstance.getDefault().getTable("dataToSend");
	NetworkTableEntry recieveDistanceFromTarget;
	NetworkTableEntry recieveCenterX;
	NetworkTableEntry recieveGetAngle;

	Double centerX;
	Double distanceFromTarget;
	Double getAngle;

	public Camera() {

	}

	public void initCamera() {
		//cameraFeed = new TargetTracker1();
	}

	public void imageProcess() {
		//cameraFeed.processImage();	
	}

	public Double returnCenterX() {
		
		recieveCenterX = dataToSendTable.getEntry("centerX");
		centerX = recieveCenterX.getDouble(-40);
		return centerX;
	}

	public Double returnDistanceFromTarget() {
		
		recieveDistanceFromTarget = dataToSendTable.getEntry("distanceFromTarget");
		distanceFromTarget = recieveDistanceFromTarget.getDouble(-30);
		
		return distanceFromTarget;
	}

	public Double returnGetAngle() {
		
		recieveGetAngle = dataToSendTable.getEntry("getAngle");
		getAngle = recieveGetAngle.getDouble(-20);
		
		return getAngle;
	}
}