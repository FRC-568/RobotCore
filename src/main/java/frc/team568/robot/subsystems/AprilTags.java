package frc.team568.robot.subsystems;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AprilTags extends SubsystemBase {

	// constants
	static AprilTagFieldLayout aprilTagFieldLayout;
	// position of cam relative to center of robot
	static Transform3d robotToCam;

	private PhotonCamera camera;
	private PhotonPipelineResult result;
	private List<PhotonTrackedTarget> targets;
	private boolean hasTargets;
	private PhotonPoseEstimator estimator;
	private Optional<EstimatedRobotPose> pose;

	private boolean driverMode;

	public AprilTags(String camName, Translation3d translation, Rotation3d rotation) {
        robotToCam = new Transform3d(translation, rotation);
        camera = new PhotonCamera(camName);
        result = new PhotonPipelineResult();
        hasTargets = false;
        pose = Optional.empty();
        driverMode = false;

        try {
            aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
            estimator = new PhotonPoseEstimator(
                    aprilTagFieldLayout,
                    PoseStrategy.AVERAGE_BEST_TARGETS,
                    camera,
                    robotToCam);
        } catch (IOException e) {
            DriverStation.reportError("Failed to load AprilTagFieldLayout: AprilTagFields.k2023ChargedUp.m_resourceFile", e.getStackTrace());
            estimator = null;
        }
	}

	public void update() {
		result = camera.getLatestResult();
		hasTargets = result.hasTargets();
		if (hasTargets) {
			targets = result.getTargets();
		}
		pose = estimator.update();
	}

    // switch between AprilTag detection and regular cam for driving
	public void setDriverMode(boolean mode) {
		driverMode = mode;
		camera.setDriverMode(driverMode);
	}

	public Optional<EstimatedRobotPose> getEstimatedPose() {
		return pose;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);

		builder.addBooleanProperty("Driver Mode", () -> driverMode, null);
		builder.addDoubleProperty("Latency", () -> result.getLatencyMillis(), null);
		builder.addBooleanProperty("Targets Exist", () -> hasTargets, null);
		// protect against NPE in case there aren't any targets in result
		if (result.hasTargets()) {
			builder.addIntegerProperty("Target Count", () -> targets.size(), null);
		}
		builder.addDoubleProperty("X Pos", () -> pose.get().estimatedPose.getX(), null);
		builder.addDoubleProperty("Y Pos", () -> pose.get().estimatedPose.getY(), null);
		builder.addDoubleProperty("Z Pos", () -> pose.get().estimatedPose.getZ(), null);
		builder.addDoubleProperty("Z Axis", () -> pose.get().estimatedPose.getRotation().getZ(), null);
	}

	@Override
	public void periodic() {
		update();
	}
}
