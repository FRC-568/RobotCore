package frc.team568.robot.crescendo.subsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import static frc.team568.robot.crescendo.Constants.VisionConstants.kMultiTagStdDevs;
import static frc.team568.robot.crescendo.Constants.VisionConstants.kSingleTagStdDevs;

public class VisionSubsystem extends SubsystemBase {
	private static final double kPoseUpdateInterval = 1.0;

	public final PhotonCamera camera;
	public final String cameraName;
	protected final PhotonPoseEstimator photonEstimator;

	private double lastEstTimestamp = 0;
	private final Collection<BiConsumer<EstimatedRobotPose, Matrix<N3, N1>>> poseListeners = new ArrayList<>();
	private final Notifier listenerThread = new Notifier(this::updatePoseListeners);

	private static String DEFAULT_CAMERA_NAME = "photonvision";
	public static final AprilTagFieldLayout kTagLayout = AprilTagFields.kDefaultField.loadAprilTagLayoutField();
	public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.5, 0.0, 0.5),
			new Rotation3d(0, 0, 0));

	private ShuffleboardTab cameraTab;
	private GenericEntry targetArea, targetId, targetPitch, targetPose, targetSkew;
	private GenericEntry poseX, poseY, poseZ;

	public VisionSubsystem() {
		this(DEFAULT_CAMERA_NAME);
	}
	
	public VisionSubsystem(String cameraName) {
		this.cameraName = cameraName;
		try {
			camera = new PhotonCamera(cameraName);
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
		cameraTab = Shuffleboard.getTab(cameraName + "_cam");

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

	public Matrix<N3, N1> getEstimationStdDevs(Pose2d estimatedPose) {
        var estStdDevs = kSingleTagStdDevs;
        var targets = getLatestResult().getTargets();
        int numTags = 0;
        double avgDist = 0;
        for (var tgt : targets) {
            var tagPose = photonEstimator.getFieldTags().getTagPose(tgt.getFiducialId());
            if (tagPose.isEmpty()) continue;
            numTags++;
            avgDist +=
                    tagPose.get().toPose2d().getTranslation().getDistance(estimatedPose.getTranslation());
        }
        if (numTags == 0) return estStdDevs;
        avgDist /= numTags;
        // Decrease std devs if multiple targets are visible
        if (numTags > 1) estStdDevs = kMultiTagStdDevs;
        // Increase std devs based on (average) distance
        if (numTags == 1 && avgDist > 4)
            estStdDevs = VecBuilder.fill(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        else estStdDevs = estStdDevs.times(1 + (avgDist * avgDist / 30));

        return estStdDevs;
    }

	public void addPoseListener(BiConsumer<EstimatedRobotPose, Matrix<N3, N1>> listener) {
		if (listener != null && !poseListeners.contains(listener))
			poseListeners.add(listener);
	}

	public void startPoseListenerThread() {
		startPoseListenerThread(kPoseUpdateInterval);
	}

	public void startPoseListenerThread(double interval) {
		listenerThread.stop();
		listenerThread.startPeriodic(interval);
	}

	public void stopPoseListenerThread() {
		listenerThread.stop();
	}

	private void updatePoseListeners() {
		var pose = getEstimatedGlobalPose();
		if (pose.isPresent()) {
			var p = pose.get();
			var stdDev = getEstimationStdDevs(p.estimatedPose.toPose2d());

			for (var l : poseListeners)
				l.accept(p, stdDev);
		}
	}

}
