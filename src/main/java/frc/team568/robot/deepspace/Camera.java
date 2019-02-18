package frc.team568.robot.deepspace;


public class Camera {
	TargetTracker1 cameraFeed;

	public Camera() {

	}

	public void initCamera() {
		cameraFeed = new TargetTracker1();
	}

	public void imageProcess() {
		cameraFeed.processImage();
	}
}