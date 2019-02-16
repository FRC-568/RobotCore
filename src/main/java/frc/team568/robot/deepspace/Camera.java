package frc.team568.robot.deepspace;


public class Camera {
	VisionTargetTracker cameraFeed;

	public Camera() {

	}

	public void initCamera() {
		cameraFeed = new VisionTargetTracker();
	}

	public void processImage() {
		cameraFeed.processImage();
	}
}