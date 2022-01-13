package frc.team568.robot.recharge;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;


@SuppressWarnings("unused")
public class Pathing {
	private static final int k_ticks_per_rev = 1024;
	private static final double k_wheel_diameter = 4.0 / 12.0;
	private static final double k_max_velocity = 10;
  
	private static final int k_left_channel = 0;
	private static final int k_right_channel = 1;
  
	private static final int k_left_encoder_port_a = 0;
	private static final int k_left_encoder_port_b = 1;
	private static final int k_right_encoder_port_a = 2;
	private static final int k_right_encoder_port_b = 3;
  
	private static final int k_gyro_port = 0;
  
	private static final String k_path_name = "pathweaver";

	public Pathing() {
		String trajectoryJSON = "paths/pathweaver.json";
		try {
  			Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
  			Trajectory trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
		} catch (IOException ex) {
  			DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
}
	}
}