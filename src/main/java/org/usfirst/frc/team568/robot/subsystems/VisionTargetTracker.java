/**
 * Based on code from https://github.com/TheGuyWhoCodes/LiftTracker
 */

package org.usfirst.frc.team568.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team568.grip.GearLifterTarget;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class VisionTargetTracker extends Subsystem {
	public static final double OFFSET_TO_FRONT = 0;
	public static final int CAMERA_WIDTH = 320; // 640;
	public static final int CAMERA_HEIGHT = 240; // 480;
	public static final double DISTANCE_CONSTANT = 5760; // 5738;
	public static final double WIDTH_BETWEEN_TARGET = 8.5; // inches

	public final String cameraName;
	public final UsbCamera camera;

	private GearLifterTarget pipeline;
	private Mat matOriginal;
	private double lengthBetweenContours;
	private double distanceFromTarget;
	private double[] centerX;

	public VisionTargetTracker() {
		this(0);
	}

	public VisionTargetTracker(final int cameraUsbPort) {
		matOriginal = new Mat();
		pipeline = new GearLifterTarget();
		camera = CameraServer.getInstance().startAutomaticCapture(cameraUsbPort);
		cameraName = camera.getName();
		camera.setResolution(CAMERA_WIDTH, CAMERA_HEIGHT);
	}

	public void processImage() {
		CameraServer.getInstance().getVideo(cameraName).grabFrame(matOriginal);
		pipeline.process(matOriginal);
		returnCenterX();
	}

	public double returnCenterX() {
		// This is the center value returned by GRIP thank WPI
		if (!pipeline.filterContoursOutput.isEmpty() && pipeline.filterContoursOutput.size() >= 2) {
			Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput.get(1));
			Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput.get(0));
			centerX = new double[] { r1.x + (r1.width / 2), r.x + (r.width / 2) };
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
		SmartDashboard.putNumber("angleToGoal", angleToGoal);
		return angleToGoal;
	}

	@Override
	protected void initDefaultCommand() {
		class ImageProcessor extends Command {
			public ImageProcessor() {
				requires(VisionTargetTracker.this);
			}

			@Override
			protected void execute() {
				// processImage();
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		}
		setDefaultCommand(new ImageProcessor());
	}

}
