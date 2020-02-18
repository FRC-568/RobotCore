package frc.team568.robot.recharge;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class ShooterAlignCommand extends CommandBase {
	TalonSRXDrive drive;
	Shooter tracker;

	Joystick controller0;

	private static final double MAX_SPEED_DRIVE = 0.5;
	private static final double MAX_SPEED_SHOOTER = 0.5;
	private static final double MAX_DISTANCE_DRIVE = 80; // inches

	NetworkTable targetDrivingData = NetworkTableInstance.getDefault().getTable("targetDrivingData");

	NetworkTableEntry speedEntry = targetDrivingData.getEntry("speed");
	NetworkTableEntry rotationEntry = targetDrivingData.getEntry("rotate speed");
	NetworkTableEntry directionEntry = targetDrivingData.getEntry("direction");
	NetworkTableEntry errorEntry = targetDrivingData.getEntry("error");
	
	public ShooterAlignCommand(TalonSRXDrive drive, RobotBase robot) {
		this.drive = drive;
		tracker = new Shooter(robot);
	}

	@Override
	public void initialize() {
		System.out.println("starting alignment");			
	}

	@Override
	public void execute() {
		final double KpDrive = 0.02;
		final double MIN_COMMAND_DRIVE = 0.05;
		//shooter
		final double KpShooter = 0.02;
		final double MIN_COMMAND_SHOOTER = 0.05;

		double driveSpeed = MAX_SPEED_DRIVE * tracker.distanceFromTarget() / MAX_DISTANCE_DRIVE;
		
		double optimalAngle = tracker.calcuateAngle();
		double currentAngle = tracker.getShooterAngle();
		double shooterRotateSpeed = MAX_SPEED_SHOOTER * currentAngle;
		
		if (driveSpeed > MAX_SPEED_DRIVE) {
			driveSpeed = MAX_SPEED_DRIVE;
			speedEntry.setDouble(driveSpeed);
		}

		if(!(optimalAngle == currentAngle)) {
			double errorShooter = currentAngle * KpShooter - MIN_COMMAND_SHOOTER;
			tracker.rotateShooterSpeed(shooterRotateSpeed - errorShooter);

			rotationEntry.setDouble(shooterRotateSpeed);
		} else {
			tracker.rotateShooterSpeed(0);
			
			rotationEntry.setDouble(0);
		}

		if (tracker.getAngle() <= 2 && tracker.getAngle() >= -2) {
			drive.tankDrive(driveSpeed, driveSpeed);
			speedEntry.setDouble(driveSpeed);
		} else {
			double errorDrive = tracker.getAngle() * KpDrive - MIN_COMMAND_DRIVE;
			drive.tankDrive(driveSpeed - errorDrive, driveSpeed + errorDrive);
			if(errorDrive > 0) {
				
				speedEntry.setDouble(driveSpeed);
				directionEntry.setString("left");
				errorEntry.setDouble(errorDrive);

				System.out.println(errorDrive);
				System.out.println("left");

			} else {
				
				speedEntry.setDouble(driveSpeed);
				directionEntry.setString("right");
				errorEntry.setDouble(errorDrive);
				
				System.out.println("right");
				System.out.println(errorDrive);

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