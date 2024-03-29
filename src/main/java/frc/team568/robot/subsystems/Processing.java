package frc.team568.robot.subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Processing {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	// Process for GRIP
	static LiftTracker tracker;
	public static VideoCapture videoCapture;
	// Constants for known variables
	static Mat matOriginal;
	public static final double OFFSET_TO_FRONT = 0;
	public static final double CAMERA_WIDTH = 640;
	public static final double DISTANCE_CONSTANT = 5738;
	public static final double WIDTH_BETWEEN_TARGET = 8.5;
	public static boolean shouldRun = true;
	static NetworkTable table;

	static double lengthBetweenContours;
	static double distanceFromTarget;
	static double lengthError;
	static double[] centerX;

	public static void main(String[] args) {
		table = NetworkTableInstance.getDefault().getTable("LiftTracker");

		while (shouldRun) {
			try {
				// opens up the camera stream and tries to load it
				videoCapture = new VideoCapture();
				tracker = new LiftTracker();
				videoCapture.open("http://roborio-1806-frc.local:1181/?action=stream");
				// change that to your team number
				// boi("http://roborio-XXXX-frc.local:1181/?action=stream");
				while (!videoCapture.isOpened()) {
					System.out.println("Didn't open Camera, restart jar");
				}
				// time to actually process the acquired images
				while (videoCapture.isOpened()) {
					processImage();
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		// make sure the java process quits when the loop finishes
		videoCapture.release();
		System.exit(0);
	}

	public static void processImage() {
		System.out.println("Processing Started");
		matOriginal = new Mat();

		// only run for the specified time
		while (true) {
			// System.out.println("Hey I'm Processing Something!");
			videoCapture.read(matOriginal);
			tracker.process(matOriginal);
			returnCenterX();
			System.out.println(getAngle());
			table.getEntry("distanceFromTarget").setDouble(distanceFromTarget());
			table.getEntry("angleFromGoal").setDouble(getAngle());
			table.getEntry("centerX").setDoubleArray(centerX);
			videoCapture.read(matOriginal);
		}

	}

	public static double returnCenterX() {
		// This is the center value returned by GRIP thank WPI
		if (!tracker.filterContoursOutput.isEmpty() && tracker.filterContoursOutput.size() >= 2) {
			Rect r = Imgproc.boundingRect(tracker.filterContoursOutput.get(1));
			Rect r1 = Imgproc.boundingRect(tracker.filterContoursOutput.get(0));
			centerX = new double[] { r1.x + (r1.width / 2), r.x + (r.width / 2) };
			Imgcodecs.imwrite("output.png", matOriginal);
			// System.out.println(centerX.length); //testing
			// this again checks for the 2 shapes on the target
			if (centerX.length == 2) {
				// subtracts one another to get length in pixels
				lengthBetweenContours = Math.abs(centerX[0] - centerX[1]);
			}
		}
		return lengthBetweenContours;
	}

	public static double distanceFromTarget() {
		// distance costant divided by length between centers of contours
		distanceFromTarget = DISTANCE_CONSTANT / lengthBetweenContours;
		return distanceFromTarget - OFFSET_TO_FRONT;
	}

	public static double getAngle() {
		// 8.5in is for the distance from center to center from goal, then
		// divide by lengthBetweenCenters in pixels to get proportion
		double constant = WIDTH_BETWEEN_TARGET / lengthBetweenContours;
		double angleToGoal = 0;
		// Looking for the 2 blocks to actually start trig
		if (!tracker.filterContoursOutput.isEmpty() && tracker.filterContoursOutput.size() >= 2) {

			if (centerX.length == 2) {
				// this calculates the distance from the center of goal to
				// center of webcam
				double distanceFromCenterPixels = ((centerX[0] + centerX[1]) / 2) - (CAMERA_WIDTH / 2);
				// Converts pixels to inches using the constant from above.
				double distanceFromCenterInch = distanceFromCenterPixels * constant;
				// math brought to you buy Chris and Jones
				angleToGoal = Math.atan(distanceFromCenterInch / distanceFromTarget());
				angleToGoal = Math.toDegrees(angleToGoal);
				// prints angle
				// System.out.println("Angle: " + angleToGoal);
			}
		}
		return angleToGoal;
	}

}
