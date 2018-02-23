package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrain2018 extends SubsystemBase {
	private Joystick joystick;

	private ADXRS450_Gyro gyro;

	// INCHES
	private static final double CIRCUMFERENCE = 18.8496;
	// Ticks per revolution
	private static final double TPR = 4096;
	// To Ticks from inches
	private static final double TO_TICKS = TPR / CIRCUMFERENCE;

	WPI_TalonSRX fl;
	WPI_TalonSRX bl;
	WPI_TalonSRX fr;
	WPI_TalonSRX br;

	int kTimeoutMs;
	int driveTargetL;
	int driveTargetR;

	public DriveTrain2018(RobotBase robot) {
		super(robot);

		gyro = new ADXRS450_Gyro();

		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		/*
		 * right = new SpeedControllerGroup(fr, br); left = new SpeedControllerGroup(fl,
		 * bl); left.setInverted(true); right.setInverted(true);
		 */

		fl.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		fr.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		fl.setInverted(false);
		bl.setInverted(false);
		fr.setInverted(true);
		br.setInverted(true);

		fl.setSensorPhase(true);
		fr.setSensorPhase(true);

		bl.follow(fl);
		br.follow(fr);

		fl.configNominalOutputForward(0, 10);
		fl.configNominalOutputReverse(0, 10);
		fl.configPeakOutputForward(1, 10);
		fl.configPeakOutputReverse(-1, 10);

		fr.configNominalOutputForward(0, 10);
		fr.configNominalOutputReverse(0, 10);
		fr.configPeakOutputForward(1, 10);
		fr.configPeakOutputReverse(-1, 10);

		fl.config_kF(0, 0.465, 10);
		fl.config_kP(0, 0.1, 10);
		fl.config_kI(0, 0, 10);
		fl.config_kD(0, 0, 10);

		fr.config_kF(0, .483, 10);
		fr.config_kP(0, 0.1, 10);
		fr.config_kI(0, 0, 10);
		fr.config_kD(0, 0, 10);

		fl.setSelectedSensorPosition(0, 0, 10);
		fr.setSelectedSensorPosition(0, 0, 10);

		// drive = new DifferentialDrive(fl, fr);

		joystick = new Joystick(0);
	}

	public void resetDist() {
		fl.setSelectedSensorPosition(0, 0, 10);
		fr.setSelectedSensorPosition(0, 0, 10);
	}

	public double getDist() {

		return (fl.getSelectedSensorPosition(0) + fr.getSelectedSensorPosition(0)) / 2;
	}

	public void resetGyro() {
		gyro.reset();

	}

	public void calGyro() {
		gyro.reset();
		gyro.calibrate();
	}

	public void driveDist(double dist, int speed, int accel) {
		int ticks = (int) (dist * TO_TICKS);
		int flCurrPos = fl.getSensorCollection().getQuadraturePosition();
		int frCurrPos = fr.getSensorCollection().getQuadraturePosition();

		driveTargetL = flCurrPos + ticks;
		driveTargetR = frCurrPos + ticks;

		fl.configMotionCruiseVelocity(speed, 10);
		fr.configMotionCruiseVelocity(speed, 10);

		fl.configMotionAcceleration(accel, 10);
		fr.configMotionAcceleration(accel, 10);

	}

	public void driveDist(double speed, double dist) {
		double Kp = .135;

		double distTarget = (dist - getDist());
		double error = gyro.getAngle() * Kp;

		double targetPercent = distTarget / dist;

		// if (targetPercent >= .75)
		// speed = speed * (1 - targetPercent);

		if (gyro.getAngle() <= 1 && gyro.getAngle() >= -1) {
			fl.set(ControlMode.PercentOutput, speed);
			fr.set(ControlMode.PercentOutput, speed);
		} else {
			fl.set(ControlMode.PercentOutput, speed - error);
			fr.set(ControlMode.PercentOutput, speed + error);
		}
		// System.out.println(gyro.getAngle());
	}

	public void driveExecute() {
		fl.set(ControlMode.Position, driveTargetL);
		fr.set(ControlMode.Position, driveTargetR);
		// System.out.println("LEFT ERROR:" + fl.getClosedLoopError(0));
		// System.out.println("RIGHT ERROR:" + fr.getClosedLoopError(0));

	}

	public boolean driveisFinished() {
		// abs??? overshoot
		// boolean isfin = (fl.getClosedLoopError(0) < 10 && fr.getClosedLoopError(0) <
		// 10);
		// boolean isfin = ((driveTargetL -
		// fl.getSensorCollection().getQuadraturePosition()) < 10 && (driveTargetR -
		// fr.getSensorCollection().getQuadraturePosition()) < 10);
		// if (isfin)
		// System.out.println("Finished!");
		return false;

	}

	public void stop() {
		fl.set(ControlMode.PercentOutput, 0);
		fr.set(ControlMode.PercentOutput, 0);
	}

	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		final double m_deadband = 0.2;

		xSpeed = limit(xSpeed);
		xSpeed = applyDeadband(xSpeed, m_deadband);

		zRotation = limit(zRotation);
		zRotation = applyDeadband(zRotation, m_deadband);

		// Square the inputs (while preserving the sign) to increase fine control
		// while permitting full power.
		if (squaredInputs) {
			xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
			zRotation = Math.copySign(zRotation * zRotation, zRotation);
		}

		double leftMotorOutput;
		double rightMotorOutput;

		double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

		if (xSpeed >= 0.0) {
			// First quadrant, else second quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			} else {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			}
		} else {
			// Third quadrant, else fourth quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			} else {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			}
		}

		fl.set(limit(leftMotorOutput));
		fr.set(limit(rightMotorOutput));
		// System.out.println("POS L" + fl.getSelectedSensorPosition(0));
		// System.out.println("POS R" + fr.getSelectedSensorPosition(0));

		// m_safetyHelper.feed();
	}

	protected double limit(double value) {
		if (value > 1.0) {
			return 1.0;
		}
		if (value < -1.0) {
			return -1.0;
		}
		return value;
	}

	protected double applyDeadband(double value, double deadband) {
		if (Math.abs(value) > deadband) {
			if (value > 0.0) {
				return (value - deadband) / (1.0 - deadband);
			} else {
				return (value + deadband) / (1.0 - deadband);
			}
		} else {
			return 0.0;
		}
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			{
				requires(DriveTrain2018.this);

			}

			@Override
			protected void execute() {
				/*
				 * drive.curvatureDrive((joystick.getRawAxis(1) * 1), (-joystick.getRawAxis(4) *
				 * .6), joystick.getRawButton(ControllerButtons.RightBumper));
				 */
				arcadeDrive(-joystick.getRawAxis(1) * .75, joystick.getRawAxis(4) * .5, false);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

	public void turnRight(double speed) {
		fl.set(ControlMode.PercentOutput, speed);
		fr.set(ControlMode.PercentOutput, -speed);
		System.out.println(getAngle());
	}

	public void turnLeft(double speed) {
		fl.set(ControlMode.PercentOutput, -speed);
		fr.set(ControlMode.PercentOutput, speed);

	}

	public double getAngle() {

		return gyro.getAngle();
	}
}