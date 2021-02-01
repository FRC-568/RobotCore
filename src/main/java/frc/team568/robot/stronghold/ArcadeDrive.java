package frc.team568.robot.stronghold;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.ReferenceFrame2016;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ArcadeDrive extends SubsystemBase {
	public final Robot robot;

	protected SpeedController leftFront, leftBack, rightFront, rightBack;

	protected Joystick driveStickL;
	protected Joystick driveStickR;

	DifferentialDrive myRobot;
	DifferentialDrive myDrive;

	private final Gyro gyro;

	static double sHeading;
	double Kp;

	public ArcadeDrive(final RobotBase robot) {
		super(robot);
		this.gyro = robot.getSubsystem(ReferenceFrame2016.class);
		this.robot = Robot.getInstance();

		leftFront = new VictorSP(port("leftFrontMotor"));
		leftBack = new VictorSP(port("leftBackMotor"));
		rightFront = new VictorSP(port("rightFrontMotor"));
		rightBack = new VictorSP(port("rightBackMotor"));

		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(true);
		rightBack.setInverted(true);

		myDrive = new DifferentialDrive(new SpeedControllerGroup(leftFront, leftBack), new SpeedControllerGroup(rightFront, rightBack));
		driveStickL = this.robot.oi.joyStick1;
		driveStickR = this.robot.oi.joyStick3;
		initDefaultCommand();
	}

	public void manualDrive() {
		if (!leftFront.getInverted() || !leftBack.getInverted()) {
			leftFront.setInverted(true);
			leftBack.setInverted(true);
		}

		if (robot.oi.trigger.get()) {
			myDrive.arcadeDrive(driveStickL.getY(), driveStickL.getX());
		} else {
			halt();
		}
		Timer.delay(0.01);
	}

	public void forwardWithGyro(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		Kp = .015;
		double error = gyro.getAngle() * Kp;

		if (gyro.getAngle() <= 5 && gyro.getAngle() >= -5) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(speed);
			rightBack.set(speed);
		} else {
			System.out.println(error);
			leftFront.set(speed - error);
			leftBack.set(speed - error);
			rightFront.set(speed + error);
			rightBack.set(speed + error);
		}

	}

	public void reverseWithGyro(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		Kp = .015;
		double error = gyro.getAngle() * Kp;
		speed = -speed;

		if (gyro.getAngle() <= 5
				&& gyro.getAngle() >= -5) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(speed);
			rightBack.set(speed);
		} else {
			System.out.println(error);
			leftFront.set(speed - error);
			leftBack.set(speed - error);
			rightFront.set(speed + error);
			rightBack.set(speed + error);
		}
	}

	public void rightWithGyro(double degrees, double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		double ra = gyro.getAngle() + degrees;
		if (gyro.getAngle() != ra) {
			leftFront.set(speed);
			leftBack.set(speed);
			rightFront.set(-speed);
			rightBack.set(-speed);
		}
	}

	public void leftWithGyro(double degrees, double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		double ra = gyro.getAngle() - degrees;
		if (gyro.getAngle() != ra) {
			leftFront.set(-speed);
			leftBack.set(-speed);
			rightFront.set(speed);
			rightBack.set(speed);
		}

	}

	public void turnLeft(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftFront.set(-speed);
		leftBack.set(-speed);
		rightFront.set(speed);
		rightBack.set(speed);

	}

	public void turnRight(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftBack.set(speed);
		leftFront.set(speed);
		rightBack.set(-speed);
		rightFront.set(-speed);

	}

	public void goForwards(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftBack.set(speed);
		leftFront.set(speed);
		rightBack.set(speed);
		rightFront.set(speed);

	}

	public void goBackwards(double speed) {
		if (leftFront.getInverted() || leftBack.getInverted()) {
			leftFront.setInverted(false);
			leftBack.setInverted(false);
		}

		leftBack.set(-speed);
		leftFront.set(-speed);
		rightBack.set(-speed);
		rightFront.set(-speed);

	}

	public void halt() {
		leftFront.set(0);
		leftBack.set(0);
		rightFront.set(0);
		rightBack.set(0);
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {
			{
				addRequirements(ArcadeDrive.this);
				SendableRegistry.addChild(ArcadeDrive.this, this);
			}

			@Override
			public void execute() {
				manualDrive();
			}

		});
	}

}
