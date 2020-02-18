package frc.team568.robot.deepspace;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class TapeTrackerCommand extends CommandBase {
	TalonSRXDrive drive;
	GripPipeline pipeline;
	Camera tracker;

	private static final double MAX_SPEED = .5;
	private static final double MAX_DISTANCE = 80; // inches

	NetworkTable targetDrivingData = NetworkTableInstance.getDefault().getTable("targetDrivingData");
	NetworkTableEntry speedEntry = targetDrivingData.getEntry("speed");
	NetworkTableEntry directionEntry = targetDrivingData.getEntry("direction");
	
	public TapeTrackerCommand(TalonSRXDrive drive) {
		this.drive = drive;
		tracker = new Camera();
	}

	@Override
	public void initialize() {
		System.out.println("starting tapeTrackerCommand");			
	}

	@Override
	public void execute() {
		final double Kp = .02;
		double speed = MAX_SPEED * tracker.returnDistanceFromTarget() / MAX_DISTANCE;
		if (speed > MAX_SPEED) {
			speed = MAX_SPEED;
			speedEntry.setDouble(speed);
		}

		if (tracker.returnGetAngle() <= 2 && tracker.returnGetAngle() >= -2) {
			//drive.tankDrive(speed, speed);
			speedEntry.setDouble(speed);
		} else {
			double error = tracker.returnGetAngle() * Kp;
			//drive.tankDrive(speed - error, speed + error);
			if(error > 0) {
				speedEntry.setDouble(speed);
				directionEntry.setString("left");
				
				System.out.println(error);
				System.out.println("left");

			} else {
				speedEntry.setDouble(speed);
				directionEntry.setString("right");
				
				System.out.println("right");
				System.out.println(error);

			}
		}
	}
	
	@Override
	public boolean isFinished() {
		// if (tracker.returnGetAngle() <= 2 && tracker.returnGetAngle() >= -2) {
		// 	return true;
		// } else {
		// 	return false;
		// }
		return false;
	}
}