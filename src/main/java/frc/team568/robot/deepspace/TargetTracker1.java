/**
 * Based on code from https://github.com/TheGuyWhoCodes/LiftTracker
 */

package frc.team568.robot.deepspace;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class TargetTracker1 extends Subsystem {
	public static final double OFFSET_TO_FRONT = 0;
	public static final int CAMERA_WIDTH = 320; // 640;
	public static final int CAMERA_HEIGHT = 240; // 480;
	public static final double DISTANCE_CONSTANT = 5760; // 5738;
	public static final double WIDTH_BETWEEN_TARGET = 13.3133853031; // inches

	public final String cameraName;
	public final UsbCamera camera;

	CvSink cvSink;
	CvSource cvSource;

	private GripPipeline pipeline;
	private Mat matOriginal;
	private Mat output;
	private double lengthBetweenContours;
	private double distanceFromTarget;
	private double[] centerX;

	NetworkTable dataToSendTable = NetworkTableInstance.getDefault().getTable("dataToSend");
	NetworkTableEntry recieveCenterX;

	public TargetTracker1() {
		this(0);
	}

	public TargetTracker1(final int cameraUsbPort) {
		matOriginal = new Mat();
		output = new Mat();
		pipeline = new GripPipeline();
		camera = CameraServer.getInstance().startAutomaticCapture(cameraUsbPort);
		cvSink = CameraServer.getInstance().getVideo();
		cvSource = CameraServer.getInstance().putVideo("findContours", CAMERA_WIDTH, CAMERA_HEIGHT);
		cameraName = camera.getName();
		camera.setResolution(CAMERA_WIDTH, CAMERA_HEIGHT);
		camera.setFPS(30);

		// SmartDashboard.putNumber("brightness", camera.getBrightness());
		// SmartDashboard.putNumber("exposure", 20);
	}

	public void processImage() {
		
		// double brightness = SmartDashboard.getNumber("brightness", 50);
		// double exposure = SmartDashboard.getNumber("exposure", 20);
		// //int brightness = 55;
		// camera.setBrightness((int) brightness);
		// camera.setExposureManual((int) exposure);
		
		
		// cvSink.grabFrame(matOriginal);
		// pipeline.process(matOriginal);
		// matOriginal.copyTo(output);
		// pipeline.filterContoursOutput().forEach(contour -> {
		// 	var box = Imgproc.boundingRect(contour);
		// 	Imgproc.rectangle(output, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height), new Scalar(200, 0, 0));
		// 	System.out.println("rectangle drawn");
		// });
		System.out.println(returnCenterX());
		//cvSink.grabFrame(matOriginal);
		//cvSource.putFrame(output);	
	}

	// @Override
	// public void initSendable(SendableBuilder builder) {
	// 	builder.setSmartDashboardType(getName());
		
	// 	builder.addDoubleProperty("upperHue", () -> pipeline.upperHue, value -> {pipeline.upperHue = value; System.out.println("upperHue");});
	// 	builder.addDoubleProperty("lowerHue", () -> pipeline.lowerHue, value -> {pipeline.lowerHue = value;});
	// 	builder.addDoubleProperty("upperSaturation", () -> pipeline.upperSaturation, value -> {pipeline.upperSaturation = value;});
	// 	builder.addDoubleProperty("lowerSaturation", () -> pipeline.lowerSaturation, value -> {pipeline.lowerSaturation = value;});
	// 	builder.addDoubleProperty("upperValue", () -> pipeline.upperValue, value -> {pipeline.upperValue = value;});
	// 	builder.addDoubleProperty("lowerValue", () -> pipeline.lowerValue, value -> {pipeline.lowerValue = value;});		

	// }

	public double returnCenterX() {
		// // This is the center value returned by GRIP thank WPI
		// if (!pipeline.filterContoursOutput().isEmpty() && pipeline.filterContoursOutput().size() >= 2) {
		// 	Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
		// 	Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
		// 	centerX = new double[] { r1.x + (r1.width / 2), r.x + (r.width / 2) };
		// 	// this again checks for the 2 shapes on the target
		// 	if (centerX.length == 2) {
		// 		// subtracts one another to get length in pixels
		// 		lengthBetweenContours = Math.abs(centerX[0] - centerX[1]);
		// 	}
		// }
			recieveCenterX = dataToSendTable.getEntry("centerX");

			recieveCenterX.getNumber(-200);

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
		if (!pipeline.filterContoursOutput().isEmpty() && pipeline.filterContoursOutput().size() >= 2) {

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
				//requires(TargetTracker1.this);
			}

			@Override
			protected void execute() {
				//processImage();
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		}
		//setDefaultCommand(new ImageProcessor());
	}

}
