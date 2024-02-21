package frc.team568.robot.crescendo.subsystem;

import java.io.IOException;
import java.util.Optional;
import java.io.File;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;



public class VisionSubsystem extends SubsystemBase {
	private ShuffleboardTab cameraTab;
	protected PhotonCamera camera;
	private final PhotonPoseEstimator photonEstimator;
	private double lastEstTimestamp = 0;

	private static String CAMERA_NAME = "photonvision";
	public static final AprilTagFieldLayout kTagLayout = AprilTagFields.kDefaultField.loadAprilTagLayoutField();
	public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.5, 0.0, 0.5), new Rotation3d(0, 0, 0));

	private int pollCount = 0;

	/**
	 * 
	 */
	public VisionSubsystem() {
		try {
			camera = new PhotonCamera(CAMERA_NAME);
			} catch(Exception e) {
				DriverStation.reportError(e.getMessage(),e.getStackTrace());
				throw new RuntimeException("Cannot create PhotonCamera object.");
			}

			
			photonEstimator =
                new PhotonPoseEstimator(
                        kTagLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, camera, kRobotToCam);
        		photonEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
		setupCameraTab();
	}

	public void setupCameraTab() {
		cameraTab = Shuffleboard.getTab( "Vision");
	}

	public void periodic()
	{
		// periodic runs every 20 milliseconds, we don't need to talk to the camera that often
		// we should consider calling the function from the swerve subsystem to better align the swerve readings with the camera readings
		if (pollCount >= 50)
		{
			getPosition();
			pollCount = 0;
		}
		else
		{
			pollCount++;
		}
	}

	/** from https://github.com/PhotonVision/photonvision/blob/master/photonlib-java-examples/swervedriveposeestsim/src/main/java/frc/robot/Vision.java
     * The latest estimated robot pose on the field from vision data. This may be empty. This should
     * only be called once per loop.
     *
     * @return An {@link EstimatedRobotPose} with an estimated pose, estimate timestamp, and targets
     *     used for estimation.
     */
    public Optional<EstimatedRobotPose> getEstimatedGlobalPose() 
	{
        var visionEst = photonEstimator.update();
        double latestTimestamp = camera.getLatestResult().getTimestampSeconds();
        boolean newResult = Math.abs(latestTimestamp - lastEstTimestamp) > 1e-5;
        if (newResult) lastEstTimestamp = latestTimestamp;
        return visionEst;
	}

	public void getPosition()
	{
		PhotonPipelineResult photonPipelineResult = camera.getLatestResult();
		if (photonPipelineResult.hasTargets())
		{
			// maybe we can look for a specific important target, like the one on the speaker wall
			var bestTarget = photonPipelineResult.getBestTarget();
			cameraTab.addDouble("Area:", () -> bestTarget.getArea());
			cameraTab.addInteger("ID:", () -> bestTarget.getFiducialId());
			cameraTab.addDouble("Pitch:", ()-> bestTarget.getPitch());
			cameraTab.addDouble("Pose:", () -> bestTarget.getPoseAmbiguity());
			cameraTab.addDouble("Skew:", () -> bestTarget.getSkew());
			//cameraTab.addString("Corners:", () -> bestTarget.getCorners().toString());
		}
		if (photonPipelineResult.getMultiTagResult().estimatedPose.isPresent)
		{
			Transform3d fieldToCamera = photonPipelineResult.getMultiTagResult().estimatedPose.best;
			cameraTab.addDouble("X: ", () -> fieldToCamera.getX());
			cameraTab.addDouble("Y: ", () -> fieldToCamera.getY());
			cameraTab.addDouble("Z: ", () -> fieldToCamera.getZ());
		}
	}
	
}

