package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.GyroSubsystem;
import frc.team568.robot.subsystems.SubsystemBase;

public class WestCoastDrive extends SubsystemBase {

	private DifferentialDrive drive;
	private Joystick joystick;
	private WPI_TalonSRX fl;
	private WPI_TalonSRX bl;
	private WPI_TalonSRX fr;
	private WPI_TalonSRX br;
	private Gyro gyro;

	// Circumference of wheel (Inches)
	private static final double WHEEL_DIAMETER = 6.0;
	private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * 3.14159265358979;
	// Ticks per revolution
	private static final double TPR = 4096;
	// Distance per ticks
	private static final double DIST_PER_TICK = WHEEL_CIRCUMFERENCE / TPR;

	private static final double CURVATURE_MINIMUM_VELOCITY = 6.0 * TPR;

	private static final int THRESHOLD_ANGLE = 2;

	public WestCoastDrive(RobotBase robot) {
		super(robot);

		initMotors();
		drive = new DifferentialDrive(new SpeedControllerGroup(fl, bl), new SpeedControllerGroup(fr, br));
		drive.setRightSideInverted(false);
		
		joystick = new Joystick(port("mainJoystick"));

		gyro = robot.getSubsystem(GyroSubsystem.class).getGyro();

		reset();

		setDefaultCommand(new DefaultCommand());
	}

	@Override
	public void periodic() {}

	protected void initMotors() {
		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		addChild("FL Motor", fl);
		addChild("BL Motor", bl);
		addChild("FR Motor", fr);
		addChild("BR Motor", br);

		fl.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		fr.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		fl.setInverted(false);
		bl.setInverted(false);
		fr.setInverted(true);
		br.setInverted(true);

		fl.setSensorPhase(true);
		fr.setSensorPhase(true);

		bl.follow(fl);
		br.follow(fr);

		fl.configNominalOutputForward(0, 0);
		fl.configNominalOutputReverse(0, 0);
		fl.configPeakOutputForward(1, 0);
		fl.configPeakOutputReverse(-1, 0);

		fr.configNominalOutputForward(0, 0);
		fr.configNominalOutputReverse(0, 0);
		fr.configPeakOutputForward(1, 0);
		fr.configPeakOutputReverse(-1, 0);

		fl.config_kF(0, 0.465, 0);
		fl.config_kP(0, 0.1, 0);
		fl.config_kI(0, 0, 0);
		fl.config_kD(0, 0, 0);

		fr.config_kF(0, .483, 0);
		fr.config_kP(0, 0.1, 0);
		fr.config_kI(0, 0, 0);
		fr.config_kD(0, 0, 0);
	}

	public void stop() {
		drive.stopMotor();
	}

	public void driveForward() {
		drive.curvatureDrive(0.5, 0, false);
	}

	public double getAverageVelocity() {
		return (fl.getSelectedSensorVelocity() + fr.getSelectedSensorVelocity()) / 2 * DIST_PER_TICK;
	}

	public double getAverageDistance() {
		return (fl.getSelectedSensorPosition() + fr.getSelectedSensorPosition()) / 2 * DIST_PER_TICK;
	}

	public void driveDist(double dist) {
		double goalDist = getAverageDistance() + dist;
		double kP = 1 / Math.abs(goalDist - getAverageDistance());
		while (goalDist > getAverageDistance()) {
			drive.curvatureDrive((goalDist - getAverageDistance()) * kP, 0, false);
		}
	}

	public void turnAngle(int angle) {
		// Setting the goal angle
		double goalAngle = gyro.getAngle();
		for (int i = 0; i < angle; i++) {
			if (angle > 0) {
				++goalAngle;
				if (goalAngle > 360)
					goalAngle = 0;
			}
			else {
				--goalAngle;
				if (goalAngle < 0)
					goalAngle = 360;
			}
		}

		// Turning the robot
		double kP = 1 / Math.abs(goalAngle - gyro.getAngle());
		while (Math.abs(goalAngle - gyro.getAngle()) < THRESHOLD_ANGLE) {
			drive.curvatureDrive(0, (goalAngle - gyro.getAngle()) * kP, true);
		}
	}

	public void reset() {
		fl.setSelectedSensorPosition(0);
		fr.setSelectedSensorPosition(0);
	}

	protected class DefaultCommand extends CommandBase {

		DefaultCommand() {
			addRequirements(WestCoastDrive.this);
			SendableRegistry.addChild(WestCoastDrive.this, this);
		}

		@Override
		public void execute() {
			double velocity = Math.max(Math.abs(fl.getSelectedSensorVelocity()),
					Math.abs(fr.getSelectedSensorVelocity()));

			drive.curvatureDrive(-joystick.getRawAxis(Xinput.LeftStickY), joystick.getRawAxis(Xinput.RightStickX),
					velocity < CURVATURE_MINIMUM_VELOCITY);
		}

	}

}
