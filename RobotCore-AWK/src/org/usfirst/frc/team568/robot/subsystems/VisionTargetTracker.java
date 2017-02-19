package org.usfirst.frc.team568.robot.subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.usfirst.frc.team568.grip.GearLifterTarget;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class VisionTargetTracker extends Subsystem {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}
	// Process for GRIP
	GearLifterTarget pipeline;
	public VideoCapture videoCapture;
	// Constants for known variables
	Mat matOriginal;
	public static final double OFFSET_TO_FRONT = 0;
	public static final double CAMERA_WIDTH = 640;
	// public static final double DISTANCE_CONSTANT= 5738;
	public static final double DISTANCE_CONSTANT = 5760;
	public static final double WIDTH_BETWEEN_TARGET = 8.5;
	public boolean shouldRun = true;
	// static NetworkTable table;

	double lengthBetweenContours;
	double distanceFromTarget;
	double lengthError;
	double[] centerX;

	public void processImage() {
		System.out.println("Processing Started");
		matOriginal = new Mat();

		// only run for the specified time
		while (true) {
			// System.out.println("Hey I'm Processing Something!");
			videoCapture.read(matOriginal);
			pipeline.process(matOriginal);
			returnCenterX();
			System.out.println(getAngle());
			// System.out.println(lengthBetweenContours);
			// table.putDouble("distanceFromTarget", distanceFromTarget());
			// table.putDouble("angleFromGoal", getAngle());
			// table.putNumberArray("centerX", centerX);
			videoCapture.read(matOriginal);
		}

	}

	public double returnCenterX() {
		double[] defaultValue = new double[0];
		// This is the center value returned by GRIP thank WPI
		if (!pipeline.filterContoursOutput.isEmpty() && pipeline.filterContoursOutput.size() >= 2) {
			Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput.get(1));
			Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput.get(0));
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

	public double distanceFromTarget() {
		// distance constant divided by length between centers of contours
		distanceFromTarget = DISTANCE_CONSTANT / lengthBetweenContours;
		return distanceFromTarget - OFFSET_TO_FRONT;
	}

	public double getAngle() {
		// 8.5in is for the distance from center to center from goal, then
		// divide by lengthBetweenCenters in pixels to get proportion
		double constant = WIDTH_BETWEEN_TARGET / lengthBetweenContours;
		double angleToGoal = 0;
		// Looking for the 2 blocks to actually start trig
		if (!pipeline.filterContoursOutput.isEmpty() && pipeline.filterContoursOutput.size() >= 2) {

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

	@Override
	protected void initDefaultCommand() {
		this.setDefaultCommand(new FrameProcessor());
		// TODO Auto-generated method stub

	}

	private class FrameProcessor extends Command {
		@Override
		protected void initialize() {
			videoCapture = new VideoCapture();
			pipeline = new GearLifterTarget();
			// videoCapture.open("http://roborio-1806-frc.local:1181/?action=stream");
			videoCapture.open(0);
		}

		@Override
		protected void execute() {
			if (videoCapture.isOpened()) {
				processImage();
			}
		}

		@Override
		protected boolean isFinished() {
			return false;
		}

		@Override
		protected void end() {
			videoCapture.release();
		}

		@Override
		protected void interrupted() {
			end();
		}

	}
}
