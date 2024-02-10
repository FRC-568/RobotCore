package frc.team568.robot.crescendo;

import java.io.IOException;
import java.io.File;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;



public class Vision extends SubsystemBase {
	private ShuffleboardTab cameraTab;
	protected PhotonCamera camera;
	protected AprilTagFieldLayout aprilTagFieldLayout;
	private static String CAMERA_NAME = "photonvision";
	private static final Pose3d CAMERA_TO_ROBOT = new Pose3d(0., 0, 0., new Rotation3d(0., 0., 0));

	/**
	 * 
	 */
	public Vision() {
		try {
			camera = new PhotonCamera(CAMERA_NAME);
			} catch(Exception e) {
				DriverStation.reportError(e.getMessage(),e.getStackTrace());
				throw new RuntimeException("Cannot create PhotonCamera object.");
			}
		try {
			aprilTagFieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
			} catch(final Exception e) {
				DriverStation.reportError(e.getMessage(),e.getStackTrace());
				throw new RuntimeException("Cannot initialize AprilTagFields.");
			}
	}

	private void setupCameraTab() {
		cameraTab = Shuffleboard.getTab( "Vision");
	}

	public void getPosition()
	{
		PhotonPipelineResult photonPipelineResult = camera.getLatestResult();
		if (photonPipelineResult.hasTargets())
		{
			// should I do this, or pull the pose?
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

