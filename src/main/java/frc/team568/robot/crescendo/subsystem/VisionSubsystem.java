package frc.team568.robot.crescendo.subsystem;

import java.util.Optional;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

public class VisionSubsystem extends SubsystemBase {
	protected PhotonCamera camera;
	private final PhotonPoseEstimator photonEstimator;
	private double lastEstTimestamp = 0;

	private static String CAMERA_NAME = "photonvision";
	public static final AprilTagFieldLayout kTagLayout = AprilTagFields.kDefaultField.loadAprilTagLayoutField();
	public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.5, 0.0, 0.5),
			new Rotation3d(0, 0, 0));

	private int pollCount = 0;

	private ShuffleboardTab cameraTab;
	private GenericEntry targetArea, targetId, targetPitch, targetPose, targetSkew;
	private GenericEntry poseX, poseY, poseZ;

	/**
	 * 
	 */
	public VisionSubsystem() {
		try {
			camera = new PhotonCamera(CAMERA_NAME);
		} catch (Exception e) {
			DriverStation.reportError(e.getMessage(), e.getStackTrace());
			throw new RuntimeException("Cannot create PhotonCamera object.");
		}

		photonEstimator = new PhotonPoseEstimator(
				kTagLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, camera, kRobotToCam);
		photonEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
		setupCameraTab();
	}

	protected void setupCameraTab() {
		cameraTab = Shuffleboard.getTab("Vision");

		targetArea = cameraTab.add("Area", 0).getEntry();
		targetId = cameraTab.add("ID", -1).getEntry();
		targetPitch = cameraTab.add("Pitch", 0).getEntry();
		targetPose = cameraTab.add("Pose", 1).getEntry();
		targetSkew = cameraTab.add("Skew", 0).getEntry();
		// targetCorners = cameraTab.add("Corners", 0).getEntry();

		poseX = cameraTab.add("X: ", 0).getEntry();
		poseY = cameraTab.add("Y: ", 0).getEntry();
		poseZ = cameraTab.add("Z: ", 0).getEntry();
	}

	public void periodic() {
		// periodic runs every 20 milliseconds, we don't need to talk to the camera that
		// often
		// we should consider calling the function from the swerve subsystem to better
		// align the swerve readings with the camera readings
		if (pollCount >= 50) {
			getLatestResult();
			pollCount = 0;
		} else {
			pollCount++;
		}
	}

	public PhotonPipelineResult getLatestResult() {
		var result = camera.getLatestResult();

		if (result.hasTargets()) {
			// maybe we can look for a specific important target, like the one on the
			// speaker wall
			var bestTarget = result.getBestTarget();
			targetArea.setDouble(bestTarget.getArea());
			targetId.setInteger(bestTarget.getFiducialId());
			targetPitch.setDouble(bestTarget.getPitch());
			targetPose.setDouble(bestTarget.getPoseAmbiguity());
			targetSkew.setDouble(bestTarget.getSkew());
			// targetCorners.setString(bestTarget.getCorners().toString());
		}
		if (result.getMultiTagResult().estimatedPose.isPresent) {
			var fieldToCamera = result.getMultiTagResult().estimatedPose.best;
			poseX.setDouble(fieldToCamera.getX());
			poseY.setDouble(fieldToCamera.getY());
			poseZ.setDouble(fieldToCamera.getZ());
		}

		return result;
	}

	/**
	 * from
	 * https://github.com/PhotonVision/photonvision/blob/master/photonlib-java-examples/swervedriveposeestsim/src/main/java/frc/robot/Vision.java
	 * The latest estimated robot pose on the field from vision data. This may be
	 * empty. This should
	 * only be called once per loop.
	 *
	 * @return An {@link EstimatedRobotPose} with an estimated pose, estimate
	 *         timestamp, and targets
	 *         used for estimation.
	 */
	public Optional<EstimatedRobotPose> getEstimatedGlobalPose() {
		var visionEst = photonEstimator.update();
		double latestTimestamp = getLatestResult().getTimestampSeconds();
		boolean newResult = Math.abs(latestTimestamp - lastEstTimestamp) > 1e-5;
		if (newResult)
			lastEstTimestamp = latestTimestamp;
		return visionEst;
	}

}
