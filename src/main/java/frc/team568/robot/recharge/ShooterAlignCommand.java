package frc.team568.robot.recharge;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class ShooterAlignCommand extends CommandBase {
	
	final TalonSRXDrive drive;
	final Shooter shooter;

	private static final double MAX_SPEED_DRIVE = 0.5;
	private static final double MAX_SPEED_SHOOTER = 0.5;
	private static final double MAX_DISTANCE_DRIVE = 80; // inches

	private double targetSpeed;
	NetworkTableEntry rotationEntry;
	NetworkTableEntry directionEntry;
	NetworkTableEntry errorEntry;
	
	public ShooterAlignCommand(final Shooter shooter, final TalonSRXDrive drive) {
		this.shooter = shooter;
		this.drive = drive;
		addRequirements(shooter, drive);
	}

	@Override
	public void execute() {
		//System.out.println("hihello");
		System.out.println(shooter.getAngle());
		//drive.arcadeDrive(0.5, 0);
		
		final double KpDrive = 0.03;
		final double MIN_COMMAND_DRIVE = 0.05;
		//shooter
		final double KpShooter = 0.02;
		final double MIN_COMMAND_SHOOTER = 0.05;

		targetSpeed = MathUtil.clamp(
			MAX_SPEED_DRIVE * shooter.distanceFromTarget() / MAX_DISTANCE_DRIVE,
			0,
			MAX_SPEED_DRIVE);
		
		// double optimalAngle = shooter.calcuateAngle();
		// double currentAngle = shooter.getShooterAngle();
		// double shooterRotateSpeed = MAX_SPEED_SHOOTER * currentAngle;

		// if (optimalAngle != currentAngle) {
		// 	double errorShooter = currentAngle * KpShooter - MIN_COMMAND_SHOOTER;
		// 	shooter.rotateShooterSpeed(shooterRotateSpeed - errorShooter);
		// 	//rotationEntry.setDouble(shooterRotateSpeed);
		// } else {
		// 	shooter.rotateShooterSpeed(0);
		// 	//rotationEntry.setDouble(0);
		// }

		while(Math.abs(shooter.distanceFromTarget()) >= 160) {
		
			if ((Math.abs(shooter.getAngle()) >= 338) && (Math.abs(shooter.getAngle()) <= 342)) {
				drive.tankDrive(targetSpeed, targetSpeed);
			} else if (Math.abs(shooter.getAngle()) < 338){
				double errorDrive = shooter.getAngle() * KpDrive - MIN_COMMAND_DRIVE;
				drive.tankDrive(targetSpeed - errorDrive, targetSpeed + errorDrive);
			} else if (Math.abs(shooter.getAngle()) > 342) {
				double errorDrive = shooter.getAngle() * KpDrive - MIN_COMMAND_DRIVE;
				drive.tankDrive(targetSpeed + errorDrive - 0.1, targetSpeed - errorDrive - 0.1);

			}
		}
			//directionEntry.setString(errorDrive > 0 ? "left" : "right");
			//errorEntry.setDouble(errorDrive);
	}
		
	
	
	@Override
	public boolean isFinished() {
		// return shooter.returnGetAngle() <= 2 && shooter.returnGetAngle() >= -2;
		return false;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Target Speed", () -> targetSpeed, null);
		rotationEntry = builder.getEntry("rotate speed");
		directionEntry = builder.getEntry("direction");
		errorEntry = builder.getEntry("error");
	}
	
}