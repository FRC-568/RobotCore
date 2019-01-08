package frc.team568.robot.subsystems;

import frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;

@SuppressWarnings("deprecation")
public class DriveTrain extends SubsystemBase {
	protected SpeedController leftFront;
	protected SpeedController leftBack;
	protected SpeedController rightFront;
	protected SpeedController rightBack;
	protected RobotDrive myDrive;

	protected Joystick driveStick1;
	private Gyro gyro;

	public DriveTrain(RobotBase robot, Gyro gyro) {
		super(robot);
		this.gyro = gyro;

		leftFront = new VictorSP(port("leftFrontMotor"));
		leftBack = new VictorSP(port("leftBackMotor"));
		rightFront = new VictorSP(port("rightFrontMotor"));
		rightBack = new VictorSP(port("rightBackMotor"));

		leftFront.setInverted(false);
		leftBack.setInverted(false);
		rightFront.setInverted(false);
		rightBack.setInverted(false);

		myDrive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);

		driveStick1 = new Joystick(0);

	}

	public void arcadeDrive() {

		myDrive.arcadeDrive((driveStick1.getRawAxis(1) * 75), (-driveStick1.getRawAxis(4) * .6));

	}

	public void tankDrive() {
		myDrive.tankDrive(driveStick1.getRawAxis(1), driveStick1.getRawAxis(5));
	}

	public void forwardWithGyro(double speed) {
		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		final double Kp = .135;

		double error = gyro.getAngle() * Kp;

		if (gyro.getAngle() <= 1 && gyro.getAngle() >= -1)
			myDrive.tankDrive(speed, speed, false);
		else
			myDrive.tankDrive(speed + error, speed - error, false);
	}

	public void reverseWithGyro(double speed) {
		final double Kp = .015;
		double error = gyro.getAngle() * Kp;
		speed = -speed;

		if (gyro.getAngle() <= 5 && gyro.getAngle() >= -5) {
			myDrive.tankDrive(speed, speed, false);
		} else {
			myDrive.tankDrive(speed - error, speed + error, false);
		}
	}

	public void turnWithGyro(double ra) {
		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		final double speed = 0.25;
		if (ra < (gyro.getAngle() + 2) || ra < (gyro.getAngle() - 2)) {
			myDrive.tankDrive(speed, -speed, false);
		} else if (ra > (gyro.getAngle() + 2) || ra > (gyro.getAngle() - 2)) {
			myDrive.tankDrive(-speed, speed, false);
		} else
			halt();
	}

	public void setSpeed(double leftValue, double rightValue) {
		myDrive.tankDrive(leftValue, rightValue);
	}

	public void turnLeft(double speed) {

		myDrive.tankDrive(-speed, speed, false);
	}

	public void turnRight(double speed) {

		myDrive.tankDrive(speed, -speed, false);
	}

	public void goForwards(double speed) {
		myDrive.tankDrive(speed, speed, false);
	}

	public void goBackwards(double speed) {
		myDrive.tankDrive(-speed, -speed, false);
	}

	public void halt() {
		myDrive.stopMotor();
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			{
				requires(DriveTrain.this);
			}

			@Override
			protected void execute() {
				arcadeDrive();
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

}
